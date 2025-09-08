package SurveySystem.controller;


import SurveySystem.entity.*;
import SurveySystem.entity.dto.SurveyDto;
import SurveySystem.service.CategoryService;
import SurveySystem.service.OptionService;
import SurveySystem.service.QuestionService;
import SurveySystem.service.SurveyService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final CategoryService categoryService;
    public SurveyController(SurveyService surveyService,
                            QuestionService questionService,
                            OptionService optionService,
                            CategoryService categoryService) {
        this.surveyService = surveyService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.categoryService = categoryService;
    }


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
    public Result<Map<String, Object>> listSurveys(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int userId,
            @RequestParam(defaultValue = "超级管理员") String role) {
        List<Survey> surveys = surveyService.getSurveysByPage(pageNum, pageSize,keyword, userId, role);
        int totalCount = surveyService.getSurveyCount(userId, keyword,role);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("surveys", surveys);
        resultMap.put("totalCount", totalCount);
        return Result.success(resultMap);
    }

    /**
     * 获取所有问卷
     * @param userId
     * @return
     */
    @GetMapping("/getAll")
    public Result<List<Survey>> getAllSurveys(@RequestParam int userId) {
        System.out.println("请求到这里了");
        List<Survey> surveys = surveyService.getAllSurveys(userId);
        return Result.success(surveys);
    }

    /**
     * 获取问卷详情
     * @param surveyId
     * @return
     */
    @GetMapping("/getSurveyAndQuestionsById")
    public Result<Map<String, Object>> getSurveyById(@RequestParam int surveyId) {
        Survey survey = surveyService.getSurveyById(surveyId);
        List<Question> questions=questionService.getQuestionsBySurveyId(surveyId);
        for(Question question:questions){
            List<Option> options= optionService.getOptionsByQuestionId(question.getQuestionId());
            question.setOptions(options);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("survey",survey);
        resultMap.put("questions",questions);
        return Result.success(resultMap);
    }


    /**
     * 保存问卷
     * @param dto
     * @return
     */
    @PostMapping("/saveBuild")
    public Result<Integer> saveSurvey(@RequestBody SurveyDto dto) {
        System.out.println("survey: "+ dto.getSurvey());

        Survey survey = dto.getSurvey();
        List<Question> questions= dto.getQuestions();
        List<Category> categories=dto.getCategories();
        for(Category category:categories){
            categoryService.updateCategorySortKey(category);
        }
        if(survey.getSurveyId()!=0){
            surveyService.updateSurvey(survey);
            handleSurveyContent(questions,survey);
        }else{
            surveyService.createSurvey(survey);
            System.out.println("surveyId: "+survey.getSurveyId());
            handleSurveyContent(questions,survey);
        }
        for(Question question:questions){
            System.out.println(question);
        }
        return Result.success(survey.getSurveyId());
    }


    /**
     * 更新问卷
     * @param dto
     * @return
     */
    @PostMapping("/updateBuild")
    public Result<Void> updateBuildSurvey(@RequestBody SurveyDto dto) {
        Survey survey = dto.getSurvey();
        List<Question> questions=dto.getQuestions();
        List<Category> categories=dto.getCategories();
        for(Category category:categories){
            System.out.println("Category: "+category);
            categoryService.updateCategorySortKey(category);
        }
        for(Question question:questions){
            System.out.println(question);
        }
        //survey.setStatus("草稿");
        surveyService.updateSurvey(survey);
        handleSurveyContent(questions,survey);
        return Result.success();
    }

    /**
     * 处理问卷内容
     * @param questions
     * @param survey
     */
    public void handleSurveyContent(List<Question> questions,Survey survey){
        System.out.println("handleSurveyContent survey: "+survey);
        for(Question question:questions){

            //问题存在，做得是更新操作。临时id可能小于0
            if(question.getQuestionId()>0){
                question.setSurveyId(survey.getSurveyId());
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
                System.out.println("handleSurveyContent");
                question.setSurveyId(survey.getSurveyId());
                questionService.addQuestionAndReturnId(question);
                // Handle open/skip options if needed
                int questionId =question.getQuestionId();
                if(question.getType().equals("排序")){
                    createRatingOption(questionId);
                }
                System.out.println("handleSurveyContent questionId: "+questionId);
                System.out.println("handleSurveyContent surveyId: "+survey.getSurveyId());

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
     * @param survey
     * @return
     */
    @PostMapping("/add")
    public Result<Void> createSurvey(@RequestBody Survey survey) {
        surveyService.createSurvey(survey);
        return Result.success();
    }

    /**
     * 更新问卷状态
     * @param survey
     * @return
     */
    @PutMapping("/update")
    public Result<Void> updateSurvey(@RequestBody Survey survey) {
        survey.setStatus("草稿");
        surveyService.updateSurvey(survey);
        return Result.success();
    }

    /**
     * 删除问卷
     * @param surveyId
     * @return
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteSurvey(@RequestParam int surveyId) {
        surveyService.deleteSurvey(surveyId);
        return Result.success();
    }
}
