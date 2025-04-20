package SurveySystem.Controller;

import SurveySystem.Model.Result;
import SurveySystem.Service.OptionService;
import SurveySystem.Model.Option;
import SurveySystem.Model.Result;
import SurveySystem.Service.OptionService;
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

    @GetMapping("/getAll")
    public Result<List<Option>> getAllOptions(@RequestParam(defaultValue = "0") int questionId,
                                              @RequestParam int userId) {
        List<Option> options = questionId == 0 ?
                optionService.getAllOptions(userId) :
                optionService.getOptionsByQuestionId(questionId);
        return Result.success(options);
    }

    @GetMapping("/getById")
    public Result<Option> getOptionById(@RequestParam int optionId) {
        Option option = optionService.getOptionById(optionId);
        return Result.success(option);
    }

    @PostMapping("/add")
    public Result<Void> createOption(@RequestBody Option option) {
        if (!"填空".equals(option.getType()) && option.getDescription() == null) {
            throw new IllegalArgumentException("Description is required for non-blank options");
        }
        optionService.addOption(option);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateOption(@RequestBody Option option) {
        option.setSkipTo(option.getIsSkip() == 0 ? 0 : option.getSkipTo());
        option.setSortKey("1"); // Maintaining your original behavior
        optionService.updateOption(option);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteOption(@RequestParam int optionId) {
        optionService.deleteOption(optionId);
        return Result.success();
    }

    @GetMapping("/getRowOptions")
    public Result<List<Option>> getRowOptions(@RequestParam int questionId) {
        List<Option> rowOptions = optionService.getRowOptionsByQuestionId(questionId);
        return Result.success(rowOptions);
    }

    @GetMapping("/getColumnOptions")
    public Result<List<Option>> getColumnOptions(@RequestParam int questionId) {
        List<Option> columnOptions = optionService.getColumnOptionsByQuestionId(questionId);
        return Result.success(columnOptions);
    }
}
