package SurveySystem.Controller;


import SurveySystem.Model.Option;
import SurveySystem.Model.Question;
import SurveySystem.Model.Result;
import SurveySystem.Model.Survey;
import SurveySystem.Model.SurveyDto;
import SurveySystem.Service.OptionService;
import SurveySystem.Service.QuestionService;
import SurveySystem.Service.SurveyService;
import lombok.Data;
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
    public SurveyController(SurveyService surveyService,
                            QuestionService questionService,
                            OptionService optionService) {
        this.surveyService = surveyService;
        this.questionService = questionService;
        this.optionService = optionService;
    }


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

    @GetMapping("/getAll")
    public Result<List<Survey>> getAllSurveys(@RequestParam int userId) {
        System.out.println("请求到这里了");
        List<Survey> surveys = surveyService.getAllSurveys(userId);
        return Result.success(surveys);
    }

    @GetMapping("/getSurveyAndQuestionsById")
    public Result<Map<String, Object>> getSurveyById(@RequestParam int surveyId) {
        Survey survey = surveyService.getSurveyById(surveyId);
        List<Question> questions=questionService.getAllQuestions(surveyId);
        for(Question question:questions){
            List<Option> options= optionService.getOptionsByQuestionId(question.getQuestionId());
            question.setOptions(options);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("survey",survey);
        resultMap.put("questions",questions);
        return Result.success(resultMap);
    }


    @PostMapping("/saveBuild")
    public Result<Integer> saveSurvey(@RequestBody SurveyDto dto) {
        System.out.println("survey: "+ dto.getSurvey());

        Survey survey = dto.getSurvey();
        List<Question> questions= dto.getQuestions();
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


    @PostMapping("/updateBuild")
    public Result<Void> updateBuildSurvey(@RequestBody SurveyDto dto) {
        Survey survey = dto.getSurvey();
        List<Question> questions=dto.getQuestions();
        for(Question question:questions){
            System.out.println(question);
        }
        survey.setStatus("草稿");
        surveyService.updateSurvey(survey);
        handleSurveyContent(questions,survey);
        return Result.success();
    }

    public void handleSurveyContent(List<Question> questions,Survey survey){
        System.out.println("handleSurveyContent survey: "+survey);
        for(Question question:questions){

            //问题存在，做得是更新操作
            if(question.getQuestionId()!=0){
                question.setSurveyId(survey.getSurveyId());
                int questionId = question.getQuestionId();
                int dataBaseIsSKip=questionService.getQuestionById(questionId).getIsSkip();
                int dataBaseIsOpen=questionService.getQuestionById(questionId).getIsOpen();
                questionService.updateQuestion(question);
                if (question.getIsOpen() == 1&&dataBaseIsOpen==0) {
                    createOpenOption(questionId);
                }
                if (question.getIsSkip() == 1&&dataBaseIsSKip==0) {
                    createSkipOption(questionId);
                }
                for(Option option:question.getOptions()){
                    String questionType =question.getType();
                    if(questionType.equals("单选")||questionType.equals("多选")||questionType.equals("评分题")
                    ||questionType.equals("排序")){
                        option.setType("行选项");
                    }
                    if(option.getOptionId()!=0){
                        option.setQuestionId(questionId);
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
                //创建开放答案
                if (question.getIsOpen() == 1) {
                    createOpenOption(questionId);
                }
                //创建跳转选项
                if (question.getIsSkip() == 1) {
                    createSkipOption(questionId);
                }

                System.out.println("handleSurveyContent questionId: "+questionId);
                System.out.println("handleSurveyContent surveyId: "+survey.getSurveyId());

                for(Option option:question.getOptions()){
                    String questionType =question.getType();
                    if(questionType.equals("单选")||questionType.equals("多选")||questionType.equals("评分题")
                            ||questionType.equals("排序")){
                        option.setType("行选项");
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

    private void createOpenOption(int questionId) {
        Option openOption = new Option();
        openOption.setQuestionId(questionId);
        openOption.setType("行选项");
        openOption.setSortKey("100");
        openOption.setDescription("其他（请填写）");
        openOption.setIsOpenOption(1);
        openOption.setIsSkip(0);
        optionService.addOption(openOption);
    }

    private void createSkipOption(int questionId) {
        Option skipOption = new Option();
        skipOption.setQuestionId(questionId);
        skipOption.setType("行选项");
        skipOption.setSortKey("100");
        skipOption.setDescription("这是跳转选项");
        skipOption.setIsOpenOption(0);
        skipOption.setIsSkip(1);
        optionService.addOption(skipOption);
    }

    @PostMapping("/add")
    public Result<Void> createSurvey(@RequestBody Survey survey) {
        surveyService.createSurvey(survey);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateSurvey(@RequestBody Survey survey) {
        survey.setStatus("草稿");
        surveyService.updateSurvey(survey);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteSurvey(@RequestParam int surveyId) {
        surveyService.deleteSurvey(surveyId);
        return Result.success();
    }
}
