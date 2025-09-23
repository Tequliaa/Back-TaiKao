package TaiExam.controller;


import TaiExam.config.RedisBloomFilter;
import TaiExam.model.dto.ExamDTO;
import TaiExam.model.entity.*;
import TaiExam.service.CategoryService;
import TaiExam.service.OptionService;
import TaiExam.service.QuestionService;
import TaiExam.service.ExamService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService examService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final CategoryService categoryService;
    public ExamController(QuestionService questionService,
                          OptionService optionService,
                          CategoryService categoryService) {
        this.questionService = questionService;
        this.optionService = optionService;
        this.categoryService = categoryService;
    }
    // 注入RedisTemplate（Spring Data Redis提供的Redis操作工具）
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // 缓存Key前缀（统一管理，避免硬编码）
    private static final String EXAM_DETAIL_KEY_PREFIX = "exam:detail:";
    // 缓存过期时间（建议30分钟-2小时，根据问卷更新频率调整，这里设1小时）
    private static final long CACHE_EXPIRE_SECONDS = 3600;

    @Autowired
    private RedisBloomFilter redisBloomFilter;

    /**
     * 获取问卷列表
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param userId
     * @param role
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listExams(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int userId,
            @RequestParam(defaultValue = "超级管理员") String role) {
        List<Exam> exams = examService.getExamsByPage(pageNum, pageSize,keyword, userId, role);
        int totalCount = examService.getExamCount(userId, keyword,role);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("exams", exams);
        resultMap.put("totalCount", totalCount);
        return Result.success(resultMap);
    }

    /**
     * 获取所有问卷
     * @param userId
     * @return
     */
    @GetMapping("/getAll")
    public Result<List<Exam>> getAllExams(@RequestParam int userId) {
        System.out.println("请求到这里了");
        List<Exam> exams = examService.getAllExams(userId);
        return Result.success(exams);
    }

    /**
     * 获取问卷详情接口
     */
    @GetMapping("/getExamAndQuestionsById")
    public Result<Map<String, Object>> getExamById(@RequestParam int id) {
        if(!redisBloomFilter.mightContain(id)){
            return Result.error("数据库内无该问卷数据。");
        }

        String cacheKey = EXAM_DETAIL_KEY_PREFIX + id;
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // 1. 先查缓存
        if (redisTemplate.hasKey(cacheKey)) {
            Object cachedExam = hashOps.get(cacheKey, "exam");
            Object cachedQuestions = hashOps.get(cacheKey, "questions");

            if (cachedExam == null) {
                return Result.error("问卷不存在");
            }

            if (!ObjectUtils.isEmpty(cachedExam) && !ObjectUtils.isEmpty(cachedQuestions)) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("exam", cachedExam);
                resultMap.put("questions", cachedQuestions);
                return Result.success(resultMap);
            }
        }

        // 2. 缓存未命中，查数据库
        Exam exam = examService.getExamById(id);
        List<Question> questions = questionService.getQuestionsByExamId(id);
        for (Question question : questions) {
            List<Option> options = optionService.getOptionsByQuestionId(question.getQuestionId());
            question.setOptions(options);
        }

        // 3. 处理空结果
        if (ObjectUtils.isEmpty(exam)) {
            hashOps.put(cacheKey, "exam", null);
            hashOps.put(cacheKey, "questions", Collections.emptyList());
            redisTemplate.expire(cacheKey, 300, TimeUnit.SECONDS);
            return Result.error("问卷不存在");
        }

        // 4. 更新缓存（供其他接口复用）
        hashOps.put(cacheKey, "exam", exam);
        hashOps.put(cacheKey, "questions", questions);
        redisTemplate.expire(cacheKey, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);

        // 5. 返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("exam", exam);
        resultMap.put("questions", questions);
        return Result.success(resultMap);
    }


    /**
     * 保存问卷
     * @param dto
     * @return
     */
    @PostMapping("/saveBuild")
    public Result<Integer> saveExam(@RequestBody ExamDTO dto) {
        System.out.println("exam: "+ dto.getExam());

        Exam exam = dto.getExam();
        List<Question> questions= dto.getQuestions();
        List<Category> categories=dto.getCategories();
        for(Category category:categories){
            categoryService.updateCategorySortKey(category);
        }
        if(exam.getId()!=0){
            examService.updateExam(exam);
            handleExamContent(questions,exam);
        }else{
            examService.createExam(exam);
            System.out.println("id: "+exam.getId());
            handleExamContent(questions,exam);
        }
        for(Question question:questions){
            System.out.println(question);
        }
        return Result.success(exam.getId());
    }


    /**
     * 更新问卷
     * @param dto
     * @return
     */
    @PostMapping("/updateBuild")
    public Result<Void> updateBuildExam(@RequestBody ExamDTO dto) {
        Exam exam = dto.getExam();
        List<Question> questions=dto.getQuestions();
        List<Category> categories=dto.getCategories();
        for(Category category:categories){
            System.out.println("Category: "+category);
            categoryService.updateCategorySortKey(category);
        }
        for(Question question:questions){
            System.out.println(question);
        }
        //exam.setStatus("草稿");
        examService.updateExam(exam);

        // 构造旧缓存Key，主动删除（让下次查询重新缓存新数据）
        String cacheKey = EXAM_DETAIL_KEY_PREFIX + exam.getId();
        redisTemplate.delete(cacheKey);

        handleExamContent(questions,exam);
        return Result.success();
    }

    /**
     * 处理问卷内容
     * @param questions
     * @param exam
     */
    public void handleExamContent(List<Question> questions,Exam exam){
        System.out.println("handleExamContent exam: "+exam);
        for(Question question:questions){

            //问题存在，做得是更新操作。临时id可能小于0
            if(question.getQuestionId()>0){
                question.setExamId(exam.getId());
                int questionId = question.getQuestionId();
                questionService.updateQuestion(question);
                for(Option option:question.getOptions()){
                    //String questionType =question.getType();
                    //if(questionType.equals("单选")||questionType.equals("多选")||questionType.equals("评分题")
                    //||questionType.equals("排序")){
                    //    option.setType("行选项");
                    //}
                    if(option.getIsOpenOption()==1||option.getIsSkip()==1){
                        option.setSortKey("100");
                    }
                    option.setQuestionId(questionId);
                    if(option.getOptionId()!=0){
                        optionService.updateOption(option);
                    }else {
                        optionService.addOption(option);
                    }
                }
            }
            //问题不存在，做的是新建问题
            else {
                System.out.println("handleExamContent");
                question.setExamId(exam.getId());
                questionService.addQuestionAndReturnId(question);
                // Handle open/skip options if needed
                int questionId =question.getQuestionId();
                if(question.getType().equals("排序")){
                    createRatingOption(questionId);
                }
                System.out.println("handleExamContent questionId: "+questionId);
                System.out.println("handleExamContent id: "+exam.getId());

                for(Option option:question.getOptions()){
                    //String questionType =question.getType();
                    //if(questionType.equals("单选")||questionType.equals("多选")||questionType.equals("评分题")
                    //        ||questionType.equals("排序")){
                    //    option.setType("行选项");
                    //}
                    option.setQuestionId(questionId);
                    if(option.getIsOpenOption()==1||option.getIsSkip()==1){
                        option.setSortKey("100");
                    }
                    option.setQuestionId(questionId);
                    if(option.getOptionId()!=0){
                        optionService.updateOption(option);
                    }else {
                        optionService.addOption(option);
                    }
                }
            }
        }
    }
    //创建评分题的选项
    private void createRatingOption(int questionId) {
        Option ratingOption = new Option();
        ratingOption.setQuestionId(questionId);
        ratingOption.setType("行选项");
        ratingOption.setSortKey("100");
        ratingOption.setDescription("请评分");
        ratingOption.setIsOpenOption(0);
        ratingOption.setIsSkip(0);
        optionService.addOption(ratingOption);
    }

    /**
     * 创建问卷
     * @param exam
     * @return
     */
    @PostMapping("/add")
    public Result<Void> createExam(@RequestBody Exam exam) {
        examService.createExam(exam);
        return Result.success();
    }

    /**
     * 更新问卷状态
     * @param exam
     * @return
     */
    @PutMapping("/update")
    public Result<Void> updateExam(@RequestBody Exam exam) {
        exam.setStatus("草稿");
        examService.updateExam(exam);
        return Result.success();
    }

    /**
     * 删除问卷
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteExam(@RequestParam int id) {
        examService.deleteExam(id);
        // 删除问卷缓存
        String cacheKey = EXAM_DETAIL_KEY_PREFIX + id;
        redisTemplate.delete(cacheKey);
        return Result.success();
    }
}
