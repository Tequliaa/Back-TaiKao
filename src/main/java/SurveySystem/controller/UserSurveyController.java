package SurveySystem.controller;

import SurveySystem.entity.Result;
import SurveySystem.entity.User;
import SurveySystem.entity.UserSurvey;
import SurveySystem.service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userSurvey")
public class UserSurveyController {

    private final UserSurveyService userSurveyService;
    private final UserService userService;
    private final SurveyService surveyService;
    private final DepartmentService departmentService;
    private final DepartmentSurveyService departmentSurveyService;

    public UserSurveyController(UserSurveyService userSurveyService, UserService userService,
                                SurveyService surveyService,DepartmentService departmentService,
                                DepartmentSurveyService departmentSurveyService) {
        this.userSurveyService = userSurveyService;
        this.userService = userService;
        this.surveyService = surveyService;
        this.departmentService = departmentService;
        this.departmentSurveyService = departmentSurveyService;
    }

    /**
     * 获取未完成的用户列表
     * @param surveyId
     * @param departmentId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws SQLException
     */
    @GetMapping("/unfinishedUsers")
    public Result<Map<String, Object>> listUnfinishedUsers(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "0") int departmentId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) throws SQLException {

        int total = userSurveyService.getUserInfoCount(surveyId, departmentId);
        List<UserSurvey> userSurveys = userSurveyService.getUserInfoBySurveyId(surveyId, departmentId, pageNum, pageSize);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("userSurveys", userSurveys);

        return Result.success(resultMap);
    }

    /**
     * 获取用户部门信息
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listUserSurveys(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "7") int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam int userId) {
        List<UserSurvey> userSurveys = userSurveyService.getSurveysByUserId(userId, keyword, pageNum, pageSize);
        int totalCount = userSurveyService.getSurveyCountByUserId(userId, keyword);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userSurveys", userSurveys);
        resultMap.put("totalCount",totalCount);

        return Result.success(resultMap);
    }

    /**
     * 发布问卷到部门
     * @param departmentId
     * @param surveyId
     * @return
     */
    @PostMapping("/assignSurvey")
    public Result<String> assignSurveyToDepartment(
            @RequestParam int departmentId,
            @RequestParam int surveyId) {

        UserSurvey userSurvey = new UserSurvey();
        userSurvey.setSurveyId(surveyId);
        userSurvey.setStatus("未完成");

        List<User> users = userService.getUsersByDepartmentId(departmentId);
        if(!departmentSurveyService.checkAssignedSurvey(surveyId,departmentId)){
            try {
                userSurveyService.assignSurveyToDepartment(users, userSurvey);
                departmentSurveyService.assignToDepartment(departmentId,surveyId);
                return Result.success("问卷发布成功");
            } catch (Exception e) {
                return Result.error("发布问卷出错了");
            }
        }else{
            return Result.error("不能向该部门重复发布问卷");
        }

    }

    /**
     * 更新问卷状态
     * @param surveyId
     * @param userId
     * @param status
     * @return
     */
    @PostMapping("/update")
    public Result<String> updateSurveyStatus(
            @RequestParam int surveyId,
            @RequestParam int userId,
            @RequestParam String status) {
        Timestamp completedAt = null;
        //打回重做
        if ("0".equals(status)) {
            completedAt = new Timestamp(System.currentTimeMillis());
            status="保存未提交";
        }

        System.out.println("status: "+status);
        try {
            userSurveyService.updateSurveyStatusBySurveyAndUser(surveyId,userId, status, completedAt);
            return Result.success("Survey status updated successfully!");
        } catch (Exception e) {
            return Result.error("Error updating survey status!");
        }
    }

    /**
     * 获取用户答卷信息
     * @param userId
     * @param surveyId
     * @return
     */
    @GetMapping("/getUserSurvey")
    public Result<UserSurvey> getUserSurveyByUserIdAndSurveyId(
            @RequestParam int userId,
            @RequestParam int surveyId) {

        UserSurvey userSurvey = userSurveyService.getUserSurveyByUserIdAndSurveyId(userId, surveyId);
        return Result.success(userSurvey);
    }

    /**
     * 导出未完成名单
     * @param surveyId
     * @param departmentId
     * @param response
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/exportUnfinishedList")
    public void exportUnfinishedList(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "0") int departmentId,
            HttpServletResponse response) throws IOException, SQLException {

        String surveyName= surveyService.getSurveyById(surveyId).getName();
        String departmentName="总体";
        if(departmentId!=0){
            departmentName=departmentService.getDepartmentById(departmentId).getName();
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = URLEncoder.encode(surveyName+"_"+departmentName+"_未完成名单-" + LocalDate.now() + ".xlsx", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 创建工作簿和工作表
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("未完成名单");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"用户名称", "所属部门", "答题状态"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 获取数据
            List<UserSurvey> unfinishedList = userSurveyService.getUserInfoBySurveyId(surveyId, departmentId, 1, Integer.MAX_VALUE);

            // 填充数据
            int rowNum = 1;
            for (UserSurvey userSurvey : unfinishedList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(userSurvey.getUsername());
                row.createCell(1).setCellValue(userSurvey.getDepartmentName());
                row.createCell(2).setCellValue(userSurvey.getStatus());
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
        }
    }
}
