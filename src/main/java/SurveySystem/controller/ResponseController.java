package SurveySystem.controller;

import SurveySystem.config.RedisBloomFilter;
import SurveySystem.entity.*;
import SurveySystem.entity.vo.OptionAnalysisVO;
import SurveySystem.entity.vo.QuestionAnalysisVO;
import SurveySystem.handler.SurveyWebSocketHandler;
import SurveySystem.service.*;
import SurveySystem.service.Impl.SurveyMessageProducer;
import SurveySystem.utils.FileUploadUtil;
import SurveySystem.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/response")
@Slf4j
public class ResponseController {

    private final ResponseService responseService;
    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final UserSurveyService userSurveyService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionAnalysisService questionAnalysisService;

    @Autowired
    private SurveyMessageProducer surveyMessageProducer;

    @Autowired
    public ResponseController(ResponseService responseService,
                            SurveyService surveyService,
                            QuestionService questionService,
                            OptionService optionService,
                            UserSurveyService userSurveyService) {
        this.responseService = responseService;
        this.surveyService = surveyService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.userSurveyService = userSurveyService;
    }

    // 注入RedisTemplate（Spring Data Redis提供的Redis操作工具）
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // 1. 复用问卷详情的缓存Key前缀（与getSurveyById接口保持一致）
    private static final String SURVEY_DETAIL_KEY_PREFIX = "survey:detail:";
    // 2. 答题详情接口独有的缓存Key前缀（存储该接口特有的数据）
    // 格式：response:details:{surveyId}:{userId}（仅存userSurvey和questionIndexMap）
    private static final String RESPONSE_DETAILS_KEY_PREFIX = "response:details:";

    // 缓存过期时间（与问卷详情缓存保持一致，便于管理）
    private static final long CACHE_EXPIRE_SECONDS = 3600;
    // 空结果缓存时间（5分钟，防穿透）
    private static final long EMPTY_CACHE_EXPIRE_SECONDS = 300;
    @Autowired
    private RedisBloomFilter redisBloomFilter;

    /**
     * 答题列表
     * @param surveyId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listResponses(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "9") int pageSize) {
        System.out.println("到ResponseList了");
        try {
            int unfinishedTotalRecords = userSurveyService.getUserInfoCount(surveyId, 0);
            List<UserSurvey> userSurveys = userSurveyService.getUserDepartmentInfoBySurveyId(surveyId);
            int totalRecords = responseService.countSurveyResponses(surveyId);
            int totalCount = (int) Math.ceil((double) totalRecords / pageSize);
            List<Response> responses = responseService.getSurveyResponsesSummary(surveyId, pageNum, pageSize);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("unfinishedTotalRecords", unfinishedTotalRecords);
            resultMap.put("userSurveys", userSurveys);
            resultMap.put("responses", responses);
            resultMap.put("totalCount", totalCount);

            return Result.success(resultMap);
        } catch (Exception e) {
            return Result.error("获取问卷响应列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/analysis")
    public SseEmitter test(
            @RequestParam(defaultValue = "0") int surveyId,
            @RequestParam(defaultValue = "0") int departmentId
    ) throws IOException {
        List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);
        List<QuestionAnalysisVO> questionAnalysisVOList = new ArrayList<>();

        for (Question question : questions) {
            QuestionAnalysisVO questionAnalysisVO = new QuestionAnalysisVO();
            questionAnalysisVO.setQuestionName(question.getDescription());
            questionAnalysisVO.setQuestionType(question.getType());

            List<Option> options = optionService.getOptionsWithCheckCountByQuestionId(question.getQuestionId(), departmentId);
            List<OptionAnalysisVO> analysisOptions=new ArrayList<>();
            options.forEach(option -> {
                OptionAnalysisVO optionAnalysisVO=new OptionAnalysisVO();
                optionAnalysisVO.setDescription(option.getDescription());
                optionAnalysisVO.setCheckCount(option.getCheckCount());
                //System.out.println("optionAnalysisVO "+optionAnalysisVO);
                analysisOptions.add(optionAnalysisVO);
            });
            questionAnalysisVO.setOptions(analysisOptions);
            questionAnalysisVOList.add(questionAnalysisVO);
        }
        SseEmitter emitter = new SseEmitter();
        questionAnalysisService.getAnalysisDataStream(questionAnalysisVOList,emitter);
        return emitter;
    }


    /**
     * 用户答题详情接口
     */
    @GetMapping("/details")
    public Result<Map<String, Object>> getResponseDetails(
            @RequestParam int surveyId,
            @RequestParam int userId) {
        System.out.println("这里是response的details");

        // 记录开始时间
        long startTime = System.currentTimeMillis();
        boolean isCacheHit = false; // 是否命中缓存

        if(!redisBloomFilter.mightContain(surveyId)){
            return Result.error("数据库内无该问卷数据。");
        }

        // 缓存Key定义
        String surveyCacheKey = SURVEY_DETAIL_KEY_PREFIX + surveyId;
        String responseCacheKey = RESPONSE_DETAILS_KEY_PREFIX + surveyId + ":" + userId;
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        try {
            // 1. 获取问卷基础数据（survey和questions）
            // 1.1 先查缓存
            Object survey = null;
            List<Question> questions = null;
            boolean isSurveyCacheValid = false;

            if (redisTemplate.hasKey(surveyCacheKey)) {
                survey = hashOps.get(surveyCacheKey, "survey");
                questions = (List<Question>) hashOps.get(surveyCacheKey, "questions");

                // 验证缓存有效性
                if (!ObjectUtils.isEmpty(survey) && !ObjectUtils.isEmpty(questions) && !questions.isEmpty()) {
                    isSurveyCacheValid = true;
                    isCacheHit = true;
                }
            }

            // 1.2 缓存无效则查数据库并更新缓存
            if (!isSurveyCacheValid) {
                // 从数据库读取
                survey = surveyService.getSurveyById(surveyId);
                questions = questionService.getQuestionsBySurveyId(surveyId);
                for (Question question : questions) {
                    List<Option> options = optionService.getOptionsByQuestionId(question.getQuestionId());
                    question.setOptions(options);
                }

                // 数据库也无数据
                if (ObjectUtils.isEmpty(survey) || ObjectUtils.isEmpty(questions)) {
                    hashOps.put(surveyCacheKey, "survey", null);
                    hashOps.put(surveyCacheKey, "questions", Collections.emptyList());
                    redisTemplate.expire(surveyCacheKey, EMPTY_CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
                    return Result.error("问卷不存在或无问题数据");
                }

                // 更新缓存
                hashOps.put(surveyCacheKey, "survey", survey);
                hashOps.put(surveyCacheKey, "questions", questions);
                redisTemplate.expire(surveyCacheKey, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 2. 获取答题详情独有的数据
            // 2.1 先查缓存
            Object userSurvey = null;
            Map<Integer, Integer> questionIndexMap = null;
            boolean isResponseCacheValid = false;

            if (redisTemplate.hasKey(responseCacheKey)) {
                userSurvey = hashOps.get(responseCacheKey, "userSurvey");
                questionIndexMap = (Map<Integer, Integer>) hashOps.get(responseCacheKey, "questionIndexMap");

                if (!ObjectUtils.isEmpty(userSurvey) && !ObjectUtils.isEmpty(questionIndexMap)) {
                    isResponseCacheValid = true;
                }
            }

            // 2.2 缓存无效则查数据库并更新缓存
            if (!isResponseCacheValid) {
                userSurvey = userSurveyService.getUserSurveyByUserIdAndSurveyId(userId, surveyId);

                // 构建问题索引Map
                questionIndexMap = new HashMap<>();
                for (int i = 0; i < questions.size(); i++) {
                    questionIndexMap.put(questions.get(i).getQuestionId(), i + 1);
                }

                // 数据库无数据
                if (ObjectUtils.isEmpty(userSurvey)) {
                    hashOps.put(responseCacheKey, "userSurvey", null);
                    hashOps.put(responseCacheKey, "questionIndexMap", Collections.emptyMap());
                    redisTemplate.expire(responseCacheKey, EMPTY_CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
                    return Result.error("用户未参与该问卷");
                }

                // 更新缓存
                hashOps.put(responseCacheKey, "userSurvey", userSurvey);
                hashOps.put(responseCacheKey, "questionIndexMap", questionIndexMap);
                redisTemplate.expire(responseCacheKey, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 3. 获取实时数据（用户答题记录）
            List<Response> userResponses = responseService.getUserResponsesForSurvey(surveyId, userId);

            // 4. 组装结果
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userResponses", userResponses); // 实时数据
            resultMap.put("userSurvey", userSurvey);       // 缓存数据
            resultMap.put("questions", questions);         // 缓存数据（复用问卷缓存）
            resultMap.put("survey", survey);               // 缓存数据（复用问卷缓存）
            resultMap.put("questionIndexMap", questionIndexMap); // 缓存数据


            // 计算响应时间（毫秒）
            long responseTime = System.currentTimeMillis() - startTime;
            // 打印日志：是否命中缓存 + 响应时间
            log.info("查询问卷ID: {}, 缓存命中: {}, 响应时间: {}ms",
                    surveyId, isCacheHit, responseTime);

            return Result.success(resultMap);

        } catch (Exception e) {
            return Result.error("获取响应详情失败：" + e.getMessage());
        }
    }

    /**
     * 问卷的统计数据
     * @param surveyId
     * @param departmentId
     * @return
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getResponseStatistics(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "0") int departmentId) {
        try {
            Survey survey = surveyService.getSurveyById(surveyId);
            List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);
            List<QuestionAnalysisVO> questionAnalysisVOList = new ArrayList<>();
            // 存储矩阵题单元格选择情况的Map
            Map<Integer, List<Map<String, Object>>> matrixCellData = new HashMap<>();

            for (Question question : questions) {
                QuestionAnalysisVO questionAnalysisVO = new QuestionAnalysisVO();
                questionAnalysisVO.setQuestionName(question.getDescription());
                questionAnalysisVO.setQuestionType(question.getType());

                List<Option> options = optionService.getOptionsWithCheckCountByQuestionId(question.getQuestionId(), departmentId);
                List<OptionAnalysisVO> analysisOptions=new ArrayList<>();
                options.forEach(option -> {
                    OptionAnalysisVO optionAnalysisVO=new OptionAnalysisVO();
                    optionAnalysisVO.setDescription(option.getDescription());
                    optionAnalysisVO.setCheckCount(option.getCheckCount());
                    //System.out.println("optionAnalysisVO "+optionAnalysisVO);
                    analysisOptions.add(optionAnalysisVO);
                });
                questionAnalysisVO.setOptions(analysisOptions);
                questionAnalysisVOList.add(questionAnalysisVO);
                question.setOptions(options);

                //if(question.getType().equals("排序")){
                //    for(Option option:question.getOptions()){
                //        System.out.println("平均排序顺序"+option.getCheckCount());
                //    }
                //}
                // 如果是矩阵题，获取单元格选择情况
                if (question.getType().equals("矩阵单选") || question.getType().equals("矩阵多选")) {
                    List<Map<String, Object>> cellData = optionService.getMatrixCellCheckCount(question.getQuestionId(), departmentId);
                    matrixCellData.put(question.getQuestionId(), cellData);
                }


            }
            // 在填充 matrixCellData 后添加日志输出
            //System.out.println("===== 矩阵题单元格数据 =====");
            //matrixCellData.forEach((questionId, cellDataList) -> {
            //    System.out.println("问题ID: " + questionId);
            //    cellDataList.forEach(cell -> {
            //        System.out.println("单元格数据: " + cell);
            //    });
            //});
            //responseService.getAnalysisData(questionAnalysisVOList);
            List<UserSurvey> userSurveys=userSurveyService.getUserDepartmentInfoBySurveyId(surveyId);

            int unfinishedTotalRecords = userSurveyService.getUserInfoCount(surveyId, departmentId);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("survey", survey);
            resultMap.put("userSurveys",userSurveys);
            resultMap.put("questions", questions);
            resultMap.put("unfinishedTotalRecords", unfinishedTotalRecords);
            resultMap.put("departmentId", departmentId);
            resultMap.put("matrixCellData", matrixCellData); // 添加矩阵单元格数据
            return Result.success(resultMap);
        } catch (Exception e) {
            return Result.error("获取响应统计失败：" + e.getMessage());
        }
    }

    /**
     * 提交问卷 - 改造为异步处理
     */
    @PostMapping("/submit")
    public Result<String> submitResponse(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "false") boolean isSaveAction,
            @RequestParam(required = false) Integer answerUserId,
            @RequestParam Map<String, String> formData,
            @RequestParam(required = false) Map<String, MultipartFile> fileMap) {

        try {
            // 获取当前用户信息
            int userId = Integer.parseInt(formData.get("userId"));
            String userRole = formData.get("userRole");
            String ipAddress = IpUtils.getClientIp();

            // 先做简单的前置检查，快速失败
            UserSurvey userSurvey = userSurveyService.getUserSurveyByUserIdAndSurveyId(userId, surveyId);
            if ("已完成".equals(userSurvey.getStatus())) {
                return Result.error("您已提交过该问卷");
            }

            // 处理文件信息，不直接传递MultipartFile
            Map<String, String> fileInfoMap = new HashMap<>();
            if (fileMap != null && !fileMap.isEmpty()) {
                for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                    MultipartFile file = entry.getValue();
                    if (!file.isEmpty()) {
                        // 使用工具类保存临时文件
                        String filePath = FileUploadUtil.saveTempFile(file);
                        // 存储文件信息，格式：路径|原始文件名|内容类型
                        fileInfoMap.put(entry.getKey(), filePath + "|" + file.getOriginalFilename() + "|" + file.getContentType());
                    }
                }
            }

            // 构建消息对象
            SurveySubmitMessage message = new SurveySubmitMessage();
            message.setSurveyId(surveyId);
            message.setSaveAction(isSaveAction);
            message.setAnswerUserId(answerUserId);
            message.setFormData(formData);
            message.setFileMap(fileInfoMap);
            message.setUserId(userId);
            message.setUserRole(userRole);
            message.setIpAddress(ipAddress);
            message.setActionType(formData.get("actionType"));
            // 构建消息对象时使用fileInfoMap

            // 发送消息到队列，异步处理
            surveyMessageProducer.sendSurveySubmitMessage(message);

            // 立即返回成功，不需要等待实际处理完成
            return Result.success("提交请求已接收，正在处理中");
        } catch (Exception e) {
            return Result.error("提交请求处理失败：" + e.getMessage());
        }
    }
}

