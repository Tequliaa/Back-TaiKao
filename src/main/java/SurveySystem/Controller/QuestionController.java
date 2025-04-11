package SurveySystem.Controller;

import SurveySystem.Model.*;
import SurveySystem.Model.Result;
import SurveySystem.Service.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final OptionService optionService;

    public QuestionController(QuestionService questionService, OptionService optionService) {
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> listQuestions(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int surveyId,
            @RequestParam(defaultValue = "0") int categoryId) {

        List<Question> questions = questionService.getQuestionsByPage(pageNum, pageSize, surveyId, categoryId, keyword);
        int totalCount = questionService.getQuestionCount(surveyId, categoryId, keyword);

        // Add options to each question
        questions.forEach(question ->
                question.setOptions(optionService.getOptionsByQuestionId(question.getQuestionId()))
        );

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("questions", questions);
        resultMap.put("totalCount", totalCount);
        return Result.success(resultMap);
    }

    @GetMapping("/getAll")
    public Result<List<Question>> getAllQuestions(@RequestParam(defaultValue = "0") int surveyId) {
        List<Question> questions = questionService.getAllQuestions(surveyId);
        questions.forEach(question ->
                question.setOptions(optionService.getOptionsByQuestionId(question.getQuestionId()))
        );
        return Result.success(questions);
    }

    @GetMapping("/getById")
    public Result<Question> getQuestionById(@RequestParam int questionId) {
        Question question = questionService.getQuestionById(questionId);
        question.setOptions(optionService.getOptionsByQuestionId(questionId));
        return Result.success(question);
    }

    @PostMapping("/add")
    public Result<Void> createQuestion(@RequestBody Question question) {
        int questionId = questionService.addQuestionAndReturnId(question);

        // Handle open/skip options if needed
        if (question.getIsOpen() == 1) {
            createOpenOption(questionId);
        } else if (question.getIsSkip() == 1) {
            createSkipOption(questionId);
        }

        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateQuestion(@RequestBody Question question) {
        questionService.updateQuestion(question);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteQuestion(@RequestParam int questionId) {
        questionService.deleteQuestion(questionId);
        return Result.success();
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
}
