package SurveySystem.Controller;


import SurveySystem.Model.Result;
import SurveySystem.Model.Survey;
import SurveySystem.Service.SurveyService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
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

    @GetMapping("/getSurveyById")
    public Result<Survey> getSurveyById(@RequestParam int surveyId) {
        Survey survey = surveyService.getSurveyById(surveyId);
        return Result.success(survey);
    }

    @PostMapping("/add")
    public Result<Void> createSurvey(@RequestBody Survey survey) {
        surveyService.createSurvey(survey);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateSurvey(@RequestBody Survey survey) {
        surveyService.updateSurvey(survey);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result<Void> deleteSurvey(@RequestParam int surveyId) {
        surveyService.deleteSurvey(surveyId);
        return Result.success();
    }
}
