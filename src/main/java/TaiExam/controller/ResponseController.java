package TaiExam.controller;

import TaiExam.config.RedisBloomFilter;
import TaiExam.model.entity.*;
import TaiExam.model.vo.OptionAnalysisVO;
import TaiExam.model.vo.QuestionAnalysisVO;
import TaiExam.rabbitmq.ExamMessageProducer;
import TaiExam.service.*;
import TaiExam.utils.FileUploadUtil;
import TaiExam.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/response")
@Slf4j
public class ResponseController {

    private final ResponseService responseService;
    private final ExamService examService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final UserExamService userExamService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionAnalysisService questionAnalysisService;

    @Autowired
    private ExamMessageProducer examMessageProducer;

    @Autowired
    public ResponseController(ResponseService responseService,
                            ExamService examService,
                            QuestionService questionService,
                            OptionService optionService,
                            UserExamService userExamService) {
        this.responseService = responseService;
        this.examService = examService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.userExamService = userExamService;
    }

    // 注入RedisTemplate（Spring Data Redis提供的Redis操作工具）
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // 1. 复用问卷详情的缓存Key前缀（与getExamById接口保持一致）
    private static final String EXAM_DETAIL_KEY_PREFIX = "exam:detail:";
    // 2. 答题详情接口独有的缓存Key前缀（存储该接口特有的数据）
    // 格式：response:details:{examId}:{userId}（仅存userExam和questionIndexMap）
    private static final String RESPONSE_DETAILS_KEY_PREFIX = "response:details:";

    // 缓存过期时间（与问卷详情缓存保持一致，便于管理）
    private static final long CACHE_EXPIRE_SECONDS = 3600;
    // 空结果缓存时间（5分钟，防穿透）
    private static final long EMPTY_CACHE_EXPIRE_SECONDS = 300;
    @Autowired
    private RedisBloomFilter redisBloomFilter;

    /**
     * 答题列表
     * @param examId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listResponses(
            @RequestParam int examId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "9") int pageSize) {
        System.out.println("到ResponseList了");
        try {
            int unfinishedTotalRecords = userExamService.getUserInfoCount(examId, 0);
            List<UserExam> userExams = userExamService.getUserDepartmentInfoByExamId(examId);
            int totalRecords = responseService.countExamResponses(examId);
            int totalCount = (int) Math.ceil((double) totalRecords / pageSize);
            List<Response> responses = responseService.getExamResponsesSummary(examId, pageNum, pageSize);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("unfinishedTotalRecords", unfinishedTotalRecords);
            resultMap.put("userExams", userExams);
            resultMap.put("responses", responses);
            resultMap.put("totalCount", totalCount);

            return Result.success(resultMap);
        } catch (Exception e) {
            return Result.error("获取问卷响应列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/analysis")
    public SseEmitter test(
            @RequestParam(defaultValue = "0") int examId,
            @RequestParam(defaultValue = "0") int departmentId
    ) throws IOException {
        List<Question> questions = questionService.getQuestionsByExamId(examId);
        List<QuestionAnalysisVO> questionAnalysisVOList = new ArrayList<>();

        for (Question question : questions) {
            QuestionAnalysisVO questionAnalysisVO = new QuestionAnalysisVO();
            questionAnalysisVO.setQuestionName(question.getDescription());
            questionAnalysisVO.setQuestionType(question.getType());

            List<Option> options = optionService.getOptionsWithCheckCountByQuestionId(question.getId(), departmentId);
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
            @RequestParam int examId,
            @RequestParam int userId) {
        System.out.println("这里是response的details");

        // 记录开始时间
        long startTime = System.currentTimeMillis();
        boolean isCacheHit = false; // 是否命中缓存

        if(!redisBloomFilter.mightContain(examId)){
            return Result.error("数据库内无该问卷数据。");
        }

        // 缓存Key定义
        String examCacheKey = EXAM_DETAIL_KEY_PREFIX + examId;
        String responseCacheKey = RESPONSE_DETAILS_KEY_PREFIX + examId + ":" + userId;
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        try {
            // 1. 获取问卷基础数据（exam和questions）
            // 1.1 先查缓存
            Object exam = null;
            List<Question> questions = null;
            boolean isExamCacheValid = false;

            if (redisTemplate.hasKey(examCacheKey)) {
                exam = hashOps.get(examCacheKey, "exam");
                questions = (List<Question>) hashOps.get(examCacheKey, "questions");

                // 验证缓存有效性
                if (!ObjectUtils.isEmpty(exam) && !ObjectUtils.isEmpty(questions) && !questions.isEmpty()) {
                    isExamCacheValid = true;
                    isCacheHit = true;
                }
            }

            // 1.2 缓存无效则查数据库并更新缓存
            if (!isExamCacheValid) {
                // 从数据库读取
                exam = examService.getExamById(examId);
                questions = questionService.getQuestionsByExamId(examId);
                for (Question question : questions) {
                    List<Option> options = optionService.getOptionsByQuestionId(question.getId());
                    question.setOptions(options);
                }

                // 数据库也无数据
                if (ObjectUtils.isEmpty(exam) || ObjectUtils.isEmpty(questions)) {
                    hashOps.put(examCacheKey, "exam", null);
                    hashOps.put(examCacheKey, "questions", Collections.emptyList());
                    redisTemplate.expire(examCacheKey, EMPTY_CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
                    return Result.error("问卷不存在或无问题数据");
                }

                // 更新缓存
                hashOps.put(examCacheKey, "exam", exam);
                hashOps.put(examCacheKey, "questions", questions);
                redisTemplate.expire(examCacheKey, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 2. 获取答题详情独有的数据
            // 2.1 先查缓存
            Object userExam = null;
            Map<Integer, Integer> questionIndexMap = null;
            boolean isResponseCacheValid = false;

            if (redisTemplate.hasKey(responseCacheKey)) {
                userExam = hashOps.get(responseCacheKey, "userExam");
                questionIndexMap = (Map<Integer, Integer>) hashOps.get(responseCacheKey, "questionIndexMap");

                if (!ObjectUtils.isEmpty(userExam) && !ObjectUtils.isEmpty(questionIndexMap)) {
                    isResponseCacheValid = true;
                }
            }

            // 2.2 缓存无效则查数据库并更新缓存
            if (!isResponseCacheValid) {
                userExam = userExamService.getUserExamByUserIdAndExamId(userId, examId);

                // 构建问题索引Map
                questionIndexMap = new HashMap<>();
                for (int i = 0; i < questions.size(); i++) {
                    questionIndexMap.put(questions.get(i).getId(), i + 1);
                }

                // 数据库无数据
                if (ObjectUtils.isEmpty(userExam)) {
                    hashOps.put(responseCacheKey, "userExam", null);
                    hashOps.put(responseCacheKey, "questionIndexMap", Collections.emptyMap());
                    redisTemplate.expire(responseCacheKey, EMPTY_CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
                    return Result.error("用户未参与该问卷");
                }

                // 更新缓存
                hashOps.put(responseCacheKey, "userExam", userExam);
                hashOps.put(responseCacheKey, "questionIndexMap", questionIndexMap);
                redisTemplate.expire(responseCacheKey, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 3. 获取实时数据（用户答题记录）
            List<Response> userResponses = responseService.getUserResponsesForExam(examId, userId);

            // 4. 组装结果
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userResponses", userResponses); // 实时数据
            resultMap.put("userExam", userExam);       // 缓存数据
            resultMap.put("questions", questions);         // 缓存数据（复用问卷缓存）
            resultMap.put("exam", exam);               // 缓存数据（复用问卷缓存）
            resultMap.put("questionIndexMap", questionIndexMap); // 缓存数据


            // 计算响应时间（毫秒）
            long responseTime = System.currentTimeMillis() - startTime;
            // 打印日志：是否命中缓存 + 响应时间
            log.info("查询问卷ID: {}, 缓存命中: {}, 响应时间: {}ms",
                    examId, isCacheHit, responseTime);

            return Result.success(resultMap);

        } catch (Exception e) {
            return Result.error("获取响应详情失败：" + e.getMessage());
        }
    }

    /**
     * 问卷的统计数据
     * @param examId
     * @param departmentId
     * @return
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getResponseStatistics(
            @RequestParam int examId,
            @RequestParam(defaultValue = "0") int departmentId) {
        // 记录开始时间和缓存命中状态
        long startTime = System.currentTimeMillis();
        boolean isCacheHit = false;

        try {
            // 1. 检查布隆过滤器，快速判断问卷是否存在
            if(!redisBloomFilter.mightContain(examId)){
                return Result.error("数据库内无该问卷数据。");
            }

            // 2. 复用问卷基础数据缓存（与details接口共用）
            String examCacheKey = EXAM_DETAIL_KEY_PREFIX + examId;
            HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

            Exam exam = null;
            List<Question> questions = null;
            boolean isExamCacheValid = false;

            // 2.1 尝试从缓存获取问卷基础数据
            if (redisTemplate.hasKey(examCacheKey)) {
                exam = (Exam) hashOps.get(examCacheKey, "exam");
                questions = (List<Question>) hashOps.get(examCacheKey, "questions");

                // 验证缓存有效性
                if (!ObjectUtils.isEmpty(exam) && !ObjectUtils.isEmpty(questions) && !questions.isEmpty()) {
                    isExamCacheValid = true;
                    isCacheHit = true;
                }
            }

            // 2.2 缓存无效则查数据库并更新缓存（与details接口逻辑一致）
            if (!isExamCacheValid) {
                exam = examService.getExamById(examId);
                questions = questionService.getQuestionsByExamId(examId);
                for (Question question : questions) {
                    List<Option> options = optionService.getOptionsByQuestionId(question.getId());
                    question.setOptions(options);
                }

                // 数据库也无数据
                if (ObjectUtils.isEmpty(exam) || ObjectUtils.isEmpty(questions)) {
                    hashOps.put(examCacheKey, "exam", null);
                    hashOps.put(examCacheKey, "questions", Collections.emptyList());
                    redisTemplate.expire(examCacheKey, EMPTY_CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
                    return Result.error("问卷不存在或无问题数据");
                }

                // 更新缓存，供后续请求复用
                hashOps.put(examCacheKey, "exam", exam);
                hashOps.put(examCacheKey, "questions", questions);
                redisTemplate.expire(examCacheKey, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 3. 处理统计接口特有数据的缓存
            String statsCacheKey = EXAM_DETAIL_KEY_PREFIX + examId + ":" + departmentId;
            Map<String, Object> statsCacheData = null;
            boolean isStatsCacheValid = false;

            // 3.1 尝试从缓存获取统计数据
            if (redisTemplate.hasKey(statsCacheKey)) {
                statsCacheData = (Map<String, Object>) redisTemplate.opsForValue().get(statsCacheKey);
                if (statsCacheData != null) {
                    isStatsCacheValid = true;
                    isCacheHit = true;
                }
            }

            // 3.2 缓存无效则计算统计数据并更新缓存
            if (!isStatsCacheValid) {
                statsCacheData = new HashMap<>();

                // 计算问题分析数据
                List<QuestionAnalysisVO> questionAnalysisVOList = new ArrayList<>();
                Map<Integer, List<Map<String, Object>>> matrixCellData = new HashMap<>();

                for (Question question : questions) {
                    QuestionAnalysisVO questionAnalysisVO = new QuestionAnalysisVO();
                    questionAnalysisVO.setQuestionName(question.getDescription());
                    questionAnalysisVO.setQuestionType(question.getType());

                    List<Option> options = optionService.getOptionsWithCheckCountByQuestionId(question.getId(), departmentId);
                    List<OptionAnalysisVO> analysisOptions = new ArrayList<>();
                    options.forEach(option -> {
                        OptionAnalysisVO optionAnalysisVO = new OptionAnalysisVO();
                        optionAnalysisVO.setDescription(option.getDescription());
                        optionAnalysisVO.setCheckCount(option.getCheckCount());
                        analysisOptions.add(optionAnalysisVO);
                    });
                    questionAnalysisVO.setOptions(analysisOptions);
                    questionAnalysisVOList.add(questionAnalysisVO);
                    question.setOptions(options);

                    // 处理矩阵题数据
                    if (question.getType().equals("矩阵单选") || question.getType().equals("矩阵多选")) {
                        List<Map<String, Object>> cellData = optionService.getMatrixCellCheckCount(question.getId(), departmentId);
                        matrixCellData.put(question.getId(), cellData);
                    }
                }

                // 获取用户参与数据
                List<UserExam> userExams = userExamService.getUserDepartmentInfoByExamId(examId);
                int unfinishedTotalRecords = userExamService.getUserInfoCount(examId, departmentId);

                // 存入缓存数据对象
                statsCacheData.put("questionAnalysisVOList", questionAnalysisVOList);
                statsCacheData.put("matrixCellData", matrixCellData);
                statsCacheData.put("userExams", userExams);
                statsCacheData.put("unfinishedTotalRecords", unfinishedTotalRecords);

                // 更新缓存
                redisTemplate.opsForValue().set(statsCacheKey, statsCacheData, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 4. 组装最终结果
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("exam", exam);                   // 复用的问卷基础数据
            resultMap.put("questions", questions);             // 复用的问题列表
            resultMap.put("questionAnalysisVOList", statsCacheData.get("questionAnalysisVOList"));
            resultMap.put("matrixCellData", statsCacheData.get("matrixCellData"));
            resultMap.put("unfinishedTotalRecords", statsCacheData.get("unfinishedTotalRecords"));
            resultMap.put("departmentId", departmentId);

            // 打印性能日志
            long responseTime = System.currentTimeMillis() - startTime;
            log.info("统计问卷ID: {}, 部门ID: {}, 缓存命中: {}, 响应时间: {}ms",
                    examId, departmentId, isCacheHit, responseTime);

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
            @RequestParam int examId,
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
            UserExam userExam = userExamService.getUserExamByUserIdAndExamId(userId, examId);
            if ("已完成".equals(userExam.getStatus())) {
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
            ExamSubmitMessage message = new ExamSubmitMessage();
            message.setId(examId);
            message.setSaveAction(isSaveAction);
            message.setAnswerUserId(answerUserId);
            message.setFormData(formData);
            message.setFileMap(fileInfoMap);
            message.setUserId(userId);
            message.setUserRole(userRole);
            message.setIpAddress(ipAddress);
            message.setActionType(formData.get("actionType"));

            // 发送消息到队列，异步处理
            examMessageProducer.sendExamSubmitMessage(message);

            // 立即返回成功，不需要等待实际处理完成
            return Result.success("提交请求已接收，正在处理中");
        } catch (Exception e) {
            return Result.error("提交请求处理失败：" + e.getMessage());
        }
    }


    @PostMapping("/test")
    public Result<String> test(
            @RequestParam int examId,
            @RequestParam(defaultValue = "false") boolean isSaveAction,
            @RequestParam(required = false) Integer answerUserId) {

        try {
            // 生成随机用户ID (1000-9999之间)
            Random random = new Random();
            int userId = 513 + random.nextInt(1000);

            // Mock基础参数
            String userRole = "普通用户";
            String ipAddress = "127.0.0.1";

            // Mock formData，保留原始结构但使用随机数据
            Map<String, String> mockFormData = new HashMap<>();
            mockFormData.put("examId", String.valueOf(examId));
            mockFormData.put("isSaveAction", String.valueOf(isSaveAction));
            mockFormData.put("userId", String.valueOf(userId));
            mockFormData.put("userRole", userRole);
            mockFormData.put("ipAddress", ipAddress);

            // Mock单选题/多选题 (随机选中状态)
            String[] multiQuestions = {
                    "question_211_optionId_564",
                    "question_220_optionId_588",
                    "question_221_optionId_593",
                    "question_221_optionId_594",
                    "question_221_optionId_595",
                    "question_221_optionId_596",
                    "question_213_optionId_570",
                    "question_224_optionId_608",
                    "question_225_optionId_611",
                    "question_225_optionId_612",
                    "question_227_optionId_624",
                    "question_229_optionId_634",
                    "question_229_optionId_633"
            };
            for (String question : multiQuestions) {
                // 70%概率选中
                mockFormData.put(question, random.nextDouble() < 0.7 ? "on" : "");
            }

            // Mock矩阵题
            String[] matrixQuestions = {
                    "question_222_row_599_col_714",
                    "question_222_row_710_col_714",
                    "question_222_row_711_col_714",
                    "question_222_row_712_col_714",
                    "question_222_row_713_col_714",
                    "question_223_row_601_col_605",
                    "question_223_row_602_col_605",
                    "question_223_row_603_col_605",
                    "question_223_row_604_col_605"
            };
            for (String question : matrixQuestions) {
                // 60%概率选中
                mockFormData.put(question, random.nextDouble() < 0.6 ? "on" : "");
            }

            // Mock评分题 (1-5分)
            mockFormData.put("rating_231_636", String.valueOf(random.nextInt(5) + 1));
            mockFormData.put("rating_232_637", String.valueOf(random.nextInt(5) + 1));

            // Mock文本题
            mockFormData.put("question_233", "测试文本" + random.nextInt(1000));
            mockFormData.put("question_212", String.valueOf(10000 + random.nextInt(90000)));

            // Mock排序题
            mockFormData.put("question_226_optionId_620", "1");
            mockFormData.put("question_226_optionId_617", "2");
            mockFormData.put("question_226_optionId_622", "3");
            mockFormData.put("question_226_optionId_619", "4");
            mockFormData.put("question_226_optionId_618", "5");
            mockFormData.put("question_226_optionId_621", "6");

            // Mock文件参数
            mockFormData.put("existing_files_230", "");

            // Mock文件信息 (不实际处理文件，只生成路径)
            Map<String, String> fileInfoMap = new HashMap<>();
            if (random.nextDouble() < 0) { // 0%概率包含文件
                String mockFileName = "mock_file_" + System.currentTimeMillis() + ".pdf";
                String mockFilePath = "/tmp/" + mockFileName;
                fileInfoMap.put("file_230", mockFilePath + "|" + mockFileName + "|application/pdf");
            }

            // 构建消息对象
            ExamSubmitMessage message = new ExamSubmitMessage();
            message.setId(examId);
            message.setSaveAction(isSaveAction);
            message.setAnswerUserId(answerUserId != null ? answerUserId : userId);
            message.setFormData(mockFormData);
            message.setFileMap(fileInfoMap);
            message.setUserId(userId);
            message.setUserRole(userRole);
            message.setIpAddress(ipAddress);
            message.setActionType("submit");

            // 发送消息到队列
            examMessageProducer.sendExamSubmitMessage(message);

            return Result.success("提交请求已接收，正在处理中（Mock数据）");
        } catch (Exception e) {
            return Result.error("提交请求处理失败：" + e.getMessage());
        }
    }

}

