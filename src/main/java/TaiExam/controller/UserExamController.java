package TaiExam.controller;

import TaiExam.model.entity.Result;
import TaiExam.model.entity.User;
import TaiExam.model.entity.UserExam;
import TaiExam.handler.ExamWebSocketHandler;
import TaiExam.service.*;
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
@RequestMapping("/userExam")
public class UserExamController {

    private final UserExamService userExamService;
    private final UserService userService;
    private final ExamService examService;
    private final DepartmentService departmentService;
    private final DepartmentExamService departmentExamService;

    public UserExamController(UserExamService userExamService, UserService userService,
                                ExamService examService, DepartmentService departmentService,
                                DepartmentExamService departmentExamService) {
        this.userExamService = userExamService;
        this.userService = userService;
        this.examService = examService;
        this.departmentService = departmentService;
        this.departmentExamService = departmentExamService;
    }

    /**
     * 获取未完成的用户列表
     * @param examId
     * @param departmentId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws SQLException
     */
    @GetMapping("/unfinishedUsers")
    public Result<Map<String, Object>> listUnfinishedUsers(
            @RequestParam int examId,
            @RequestParam(defaultValue = "0") int departmentId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize) throws SQLException {

        int total = userExamService.getUserInfoCount(examId, departmentId);
        List<UserExam> userExams = userExamService.getUserInfoByExamId(examId, departmentId, pageNum, pageSize);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("userExams", userExams);

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
    public Result<Map<String, Object>> listUserExams(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "7") int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam int userId) {
        List<UserExam> userExams = userExamService.getExamsByUserId(userId, keyword, pageNum, pageSize);
        int totalCount = userExamService.getExamCountByUserId(userId, keyword);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userExams", userExams);
        resultMap.put("totalCount",totalCount);

        return Result.success(resultMap);
    }

    /**
     * 发布问卷到部门
     * @param departmentId
     * @param examId
     * @return
     */
    @PostMapping("/assignExam")
    public Result<String> assignExamToDepartment(
            @RequestParam int departmentId,
            @RequestParam int examId) {

        UserExam userExam = new UserExam();
        userExam.setExamId(examId);
        userExam.setStatus("未完成");

        List<User> users = userService.getUsersByDepartmentId(departmentId);
        if(!departmentExamService.checkAssignedExam(examId,departmentId)){
            try {
                userExamService.assignExamToDepartment(users, userExam);
                departmentExamService.assignToDepartment(departmentId,examId);
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
     * @param examId
     * @param userId
     * @param status
     * @return
     */
    @PostMapping("/update")
    public Result<String> updateExamStatus(
            @RequestParam int examId,
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
            userExamService.updateExamStatusByExamAndUser(examId,userId, status, completedAt);
            User user = userService.getUserByUserId(userId);
            int newUnfinishedCount = userExamService.getUserInfoCount(
                    examId, user.getDepartmentId());
            // 准备通知数据（添加 examId 到消息体中）
            Map<String, Object> pushData = new HashMap<>();
            pushData.put("examId", examId); // 新增：将 examId 放入消息体
            pushData.put("unfinishedTotalRecords", newUnfinishedCount);
            pushData.put("message", "用户【" + user.getName() + "】问卷被打回");
            // 处理WebSocket广播
            ExamWebSocketHandler.broadcastToExam(examId, pushData);
            return Result.success("Exam status updated successfully!");
        } catch (Exception e) {
            return Result.error("Error updating exam status!");
        }
    }

    /**
     * 获取用户答卷信息
     * @param userId
     * @param examId
     * @return
     */
    @GetMapping("/getUserExam")
    public Result<UserExam> getUserExamByUserIdAndExamId(
            @RequestParam int userId,
            @RequestParam int examId) {

        UserExam userExam = userExamService.getUserExamByUserIdAndExamId(userId, examId);
        return Result.success(userExam);
    }

    /**
     * 导出未完成名单
     * @param examId
     * @param departmentId
     * @param response
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/exportUnfinishedList")
    public void exportUnfinishedList(
            @RequestParam int examId,
            @RequestParam(defaultValue = "0") int departmentId,
            HttpServletResponse response) throws IOException, SQLException {

        String examName= examService.getExamById(examId).getName();
        String departmentName="总体";
        if(departmentId!=0){
            departmentName=departmentService.getDepartmentById(departmentId).getName();
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = URLEncoder.encode(examName+"_"+departmentName+"_未完成名单-" + LocalDate.now() + ".xlsx", StandardCharsets.UTF_8);
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
            List<UserExam> unfinishedList = userExamService.getUserInfoByExamId(examId, departmentId, 1, Integer.MAX_VALUE);

            // 填充数据
            int rowNum = 1;
            for (UserExam userExam : unfinishedList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(userExam.getUsername());
                row.createCell(1).setCellValue(userExam.getDepartmentName());
                row.createCell(2).setCellValue(userExam.getStatus());
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
