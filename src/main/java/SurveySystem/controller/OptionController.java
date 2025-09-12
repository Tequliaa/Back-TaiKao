package SurveySystem.controller;

import SurveySystem.entity.Result;
import SurveySystem.service.OptionService;
import SurveySystem.entity.Option;
import SurveySystem.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/option")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Autowired
    private QuestionService questionService;

    /**
     * 分页获取选项列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param keyword
     * @param questionId
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listOptions(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam int userId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int questionId) {

        List<Option> options = optionService.getOptionsByPage(pageNum, pageSize, questionId,userId);
        int totalCount = optionService.getOptionCount(questionId,userId);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("options", options);
        resultMap.put("totalCount", totalCount);

        return Result.success(resultMap);
    }

    /**
     * 获取选项列表
     * @param questionId
     * @param userId
     * @return
     */
    @GetMapping("/getAll")
    public Result<List<Option>> getAllOptions(@RequestParam(defaultValue = "0") int questionId,
                                              @RequestParam int userId) {
        List<Option> options = questionId == 0 ?
                optionService.getAllOptions(userId) :
                optionService.getOptionsByQuestionId(questionId);
        return Result.success(options);
    }

    /**
     * 根据问题ID获取选项列表
     * @param optionId
     * @return
     */
    @GetMapping("/getById")
    public Result<Option> getOptionById(@RequestParam int optionId) {
        Option option = optionService.getOptionById(optionId);
        return Result.success(option);
    }

    /**
     * 创建选项
     * @param option
     * @return
     */
    @PostMapping("/add")
    public Result<Void> createOption(@RequestBody Option option) {
        if (!"填空".equals(option.getType()) && option.getDescription() == null) {
            throw new IllegalArgumentException("Description is required for non-blank options");
        }
        int surveyId = questionService.getQuestionById(option.getQuestionId()).getSurveyId();
        option.setSurveyId(surveyId);
        optionService.addOption(option);
        return Result.success();
    }

    /**
     * 更新选项
     * @param option
     * @return
     */
    @PutMapping("/update")
    public Result<Void> updateOption(@RequestBody Option option) {
        option.setSkipTo(option.getIsSkip() == 0 ? 0 : option.getSkipTo());
        option.setSortKey("1"); // Maintaining your original behavior
        int surveyId = questionService.getQuestionById(option.getQuestionId()).getSurveyId();
        option.setSurveyId(surveyId);
        optionService.updateOption(option);
        return Result.success();
    }

    /**
     * 删除选项
     * @param optionId
     * @return
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteOption(@RequestParam int optionId) {
        Option option = optionService.getOptionById(optionId);
        int surveyId = questionService.getQuestionById(option.getQuestionId()).getSurveyId();
        option.setSurveyId(surveyId);
        optionService.deleteOption(optionId);
        return Result.success();
    }

    /**
     * 获取行选项列表
     * @param questionId
     * @return
     */
    @GetMapping("/getRowOptions")
    public Result<List<Option>> getRowOptions(@RequestParam int questionId) {
        List<Option> rowOptions = optionService.getRowOptionsByQuestionId(questionId);
        return Result.success(rowOptions);
    }

    /**
     * 获取列选项列表
     * @param questionId
     * @return
     */
    @GetMapping("/getColumnOptions")
    public Result<List<Option>> getColumnOptions(@RequestParam int questionId) {
        List<Option> columnOptions = optionService.getColumnOptionsByQuestionId(questionId);
        return Result.success(columnOptions);
    }
}
