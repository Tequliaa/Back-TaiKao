package SurveySystem.Controller;

import SurveySystem.Model.*;
import SurveySystem.Service.*;
import SurveySystem.Utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/response")
public class ResponseController {

    private final ResponseService responseService;
    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final UserSurveyService userSurveyService;

    @Autowired
    public ResponseController(ResponseService responseService,
                            SurveyService surveyService,
                            QuestionService questionService,
                            OptionService optionService,
                            UserSurveyService userSurveyService) {
        this.responseService = responseService;
        this.surveyService = surveyService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.userSurveyService = userSurveyService;
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> listResponses(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "9") int pageSize) {
        System.out.println("到ResponseList了");
        try {
            int unfinishedTotalRecords = userSurveyService.getUserInfoCount(surveyId, 0);
            List<UserSurvey> userSurveys = userSurveyService.getUserDepartmentInfoBySurveyId(surveyId);
            int totalRecords = responseService.countSurveyResponses(surveyId);
            int totalCount = (int) Math.ceil((double) totalRecords / pageSize);
            List<Response> responses = responseService.getSurveyResponsesSummary(surveyId, pageNum, pageSize);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("unfinishedTotalRecords", unfinishedTotalRecords);
            resultMap.put("userSurveys", userSurveys);
            resultMap.put("responses", responses);
            resultMap.put("totalCount", totalCount);

            return Result.success(resultMap);
        } catch (Exception e) {
            return Result.error("获取问卷响应列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/details")
    public Result<Map<String, Object>> getResponseDetails(
            @RequestParam int surveyId,
            @RequestParam int userId) {
        System.out.println("这里是response的details");
        try {
            List<Response> userResponses = responseService.getUserResponsesForSurvey(surveyId, userId);
            UserSurvey userSurvey = userSurveyService.getUserSurveyByUserIdAndSurveyId(userId, surveyId);
            List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);

            Map<Integer, Integer> questionIndexMap = new HashMap<>();
            for (int i = 0; i < questions.size(); i++) {
                questionIndexMap.put(questions.get(i).getQuestionId(), i + 1);
            }

            for (Question question : questions) {
                List<Option> options = optionService.getOptionsByQuestionId(question.getQuestionId());
                question.setOptions(options);
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userResponses", userResponses);
            resultMap.put("userSurvey", userSurvey);
            resultMap.put("questions", questions);
            resultMap.put("questionIndexMap", questionIndexMap);

            return Result.success(resultMap);
        } catch (Exception e) {
            return Result.error("获取响应详情失败：" + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getResponseStatistics(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "0") int departmentId) {
        try {
            Survey survey = surveyService.getSurveyById(surveyId);
            List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);

            // 存储矩阵题单元格选择情况的Map
            Map<Integer, List<Map<String, Object>>> matrixCellData = new HashMap<>();

            for (Question question : questions) {
                List<Option> options = optionService.getOptionsWithCheckCountByQuestionId(question.getQuestionId(), departmentId);
                question.setOptions(options);
                //if(question.getType().equals("排序")){
                //    for(Option option:question.getOptions()){
                //        System.out.println("平均排序顺序"+option.getCheckCount());
                //    }
                //}
                // 如果是矩阵题，获取单元格选择情况
                if (question.getType().equals("矩阵单选") || question.getType().equals("矩阵多选")) {
                    List<Map<String, Object>> cellData = optionService.getMatrixCellCheckCount(question.getQuestionId(), departmentId);
                    matrixCellData.put(question.getQuestionId(), cellData);
                }
            }
            // 在填充 matrixCellData 后添加日志输出
            //System.out.println("===== 矩阵题单元格数据 =====");
            //matrixCellData.forEach((questionId, cellDataList) -> {
            //    System.out.println("问题ID: " + questionId);
            //    cellDataList.forEach(cell -> {
            //        System.out.println("单元格数据: " + cell);
            //    });
            //});
            List<UserSurvey> userSurveys=userSurveyService.getUserDepartmentInfoBySurveyId(surveyId);

            int unfinishedTotalRecords = userSurveyService.getUserInfoCount(surveyId, departmentId);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("survey", survey);
            resultMap.put("userSurveys",userSurveys);
            resultMap.put("questions", questions);
            resultMap.put("unfinishedTotalRecords", unfinishedTotalRecords);
            resultMap.put("departmentId", departmentId);
            resultMap.put("matrixCellData", matrixCellData); // 添加矩阵单元格数据
            return Result.success(resultMap);
        } catch (Exception e) {
            return Result.error("获取响应统计失败：" + e.getMessage());
        }
    }

    @PostMapping("/submit")
    public Result<Void> submitResponse(
            @RequestParam int surveyId,
            @RequestParam(defaultValue = "false") boolean isSaveAction,
            @RequestParam(required = false) Integer answerUserId,
            @RequestParam Map<String, String> formData,
            @RequestParam(required = false) Map<String, MultipartFile> fileMap) {
        System.out.println("到submit了");
        try {
            // 获取当前用户信息
            int userId = Integer.parseInt(formData.get("userId"));
            String userRole = formData.get("userRole");
            //String ipAddress = formData.get("ipAddress");
            String ipAddress = IpUtils.getClientIp();

            // 处理重新提交的情况
            if ("remake".equals(formData.get("actionType")) && "超级管理员".equals(userRole)) {
                if (answerUserId != null) {
                    remakeSurvey(surveyId, answerUserId);
                    return Result.success();
                }
                return Result.error("重新提交失败：未指定用户ID");
            }

            // 检查是否已提交
            UserSurvey userSurvey = userSurveyService.getUserSurveyByUserIdAndSurveyId(userId, surveyId);
            if ("已完成".equals(userSurvey.getStatus())) {
                return Result.error("您已提交过该问卷");
            }

            // 初始化响应记录
            initializeResponses(surveyId, userId, ipAddress);

            if(!formData.isEmpty()&&formData!=null){
                // 处理表单数据
                processFormData(formData, surveyId, userId, ipAddress, isSaveAction);
            }

            // 处理文件上传
            if (fileMap != null && !fileMap.isEmpty()) {
                handleFileUploads(fileMap, surveyId, userId, ipAddress);
            }

            // 完成问卷
            completeSurvey(surveyId, userId, isSaveAction);

            return Result.success();
        } catch (Exception e) {
            return Result.error("提交响应失败：" + e.getMessage());
        }
    }

    private void handleFileUploads(Map<String, MultipartFile> fileMap, int surveyId, int userId, String ipAddress) throws IOException {
        System.out.println("有新文件上传");

        // 根据操作系统确定上传路径 - 与WebConfig保持一致
        String os = System.getProperty("os.name").toLowerCase();
        String uploadPath;

        if (os.contains("win")) {
            uploadPath = "F:/uploads/"; // Windows路径
        } else {
            uploadPath = "/var/www/uploads/"; // Linux路径
        }

        // 确保目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        List<String> allowedFileTypes = Arrays.asList("jpg", "jpeg", "png", "gif", "pdf", "docx", "xlsx");

        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String paramName = entry.getKey();
            MultipartFile file = entry.getValue();

            if (!file.isEmpty()) {
                int questionId = Integer.parseInt(paramName.split("_")[1]);
                String fileExtension = file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                        .toLowerCase();

                if (!allowedFileTypes.contains(fileExtension)) {
                    throw new IllegalArgumentException("不支持的文件类型: " + fileExtension);
                }

                if (file.getSize() > 20 * 1024 * 1024) {
                    throw new IllegalArgumentException("文件过大，请上传小于 20MB 的文件");
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File destFile = new File(uploadDir, fileName);

                try {
                    file.transferTo(destFile.toPath().toAbsolutePath());

                    // 记录文件路径 - 使用WebConfig中配置的映射路径
                    String filePath = "/uploads/" + fileName;
                    System.out.println("文件保存到: " + destFile.getAbsolutePath());
                    System.out.println("访问路径: " + filePath);

                    Response response = new Response();
                    response.setSurveyId(surveyId);
                    response.setQuestionId(questionId);
                    response.setUserId(userId);
                    response.setIsValid(1);
                    response.setIpAddress(ipAddress);
                    response.setFilePath(filePath);
                    responseService.saveFilePathToDatabase(response);
                } catch (IOException e) {
                    System.err.println("文件保存失败: " + e.getMessage());
                    throw e;
                }
            }
        }
    }


    private void processFormData(Map<String, String> formData, int surveyId, int userId, String ipAddress, boolean isSaveAction) throws Exception {
        // //调试输出开始
        //System.out.println("=== 完整的formData内容 ===");
        //for (Map.Entry<String, String> entry : formData.entrySet()) {
        //    System.out.println(entry.getKey() + " = " + entry.getValue());
        //}
        //System.out.println("=======================");
        // 调试输出结束
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            if (paramName.startsWith("question_")) {
                int questionId = Integer.parseInt(paramName.split("_")[1]);
                Question question=questionService.getQuestionById(questionId);
                if ("排序".equals(question.getType())) {
                    //System.out.println("处理排序题");
                    processSortAnswer(paramName, paramValue, surveyId, questionId, ipAddress, userId);
                }
                else{
                    processQuestionAnswer(paramName,paramValue, surveyId, questionId, ipAddress, userId, isSaveAction);
                }
            } else if (paramName.startsWith("rating_")) {
                processRatingAnswer(paramName, paramValue, surveyId, ipAddress, userId);
            } else if (paramName.startsWith("open_answer_")) {
                processOpenAnswer(paramName, paramValue);
            } else if (paramName.startsWith("existing_files_")) {
                System.out.println("到existing_files_了");
                System.out.println("existing_files_ ——————"+paramValue);
                // 处理文件上传题中的已有文件
                int questionId = Integer.parseInt(paramName.split("_")[2]);
                processExistingFiles(questionId, paramValue);
            }
        }
    }

    //处理排序题
    private void processSortAnswer(String paramName, String paramValue, int surveyId, int questionId,
                                   String ipAddress, int userId) throws Exception {
        // 从参数名中提取选项ID
        int optionId = Integer.parseInt(paramName.split("_")[3]);
        //System.out.println("optionId: "+optionId);
        // 从参数值中获取排序位置
        int sortOrder = Integer.parseInt(paramValue);
        //System.out.println("sortOrder: "+sortOrder);
        Response response = new Response();
        response.setSurveyId(surveyId);
        response.setQuestionId(questionId);
        response.setOptionId(optionId);
        response.setSortOrder(sortOrder);  // 设置排序位置
        response.setResponseData("");
        response.setIsValid(1);
        response.setRowId(0);
        response.setColumnId(0);
        response.setUserId(userId);
        response.setIpAddress(ipAddress);
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        responseService.updateResponse(response);
    }

    private void processQuestionAnswer(String paramName,String paramValue, int surveyId, int questionId,
                                     String ipAddress, int userId, boolean isSaveAction) throws Exception {
        if (paramName.contains("_row_")) {
            String rowOptionId = paramName.split("_")[3];
            String columnOptionId = paramName.split("_")[5];
            saveMatrixResponse(surveyId, questionId, rowOptionId, columnOptionId, ipAddress, userId, isSaveAction);
        } else {
            saveResponse(surveyId, questionId,paramName, paramValue, ipAddress, userId, isSaveAction);
        }
    }

    private void processRatingAnswer(String paramName, String paramValue, int surveyId, String ipAddress, int userId) throws Exception {
        String[] parts = paramName.split("_");
        int questionId = Integer.parseInt(parts[1]);
        int optionId = Integer.parseInt(parts[2]);

        Response response = new Response();
        response.setSurveyId(surveyId);
        response.setQuestionId(questionId);
        response.setOptionId(optionId);
        response.setRowId(0);
        response.setIsValid(1);
        response.setColumnId(0);
        response.setResponseData(paramValue);
        response.setUserId(userId);
        response.setIpAddress(ipAddress);
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        responseService.updateResponse(response);
    }

    private void processOpenAnswer(String paramName, String paramValue){
        int optionId = Integer.parseInt(paramName.split("_")[2]);
        Response response = new Response();
        response.setOptionId(optionId);
        response.setResponseData(paramValue);
        response.setIsValid(paramValue == null || "".equals(paramValue) ? 0 : 1);
        responseService.updateResponseData(response);
    }
    //处理单选多选和填空
    private void saveResponse(int surveyId, int questionId,String paramName, String answer, String ipAddress, int userId, boolean isSaveAction) throws Exception {
        Response response = new Response();
        response.setSurveyId(surveyId);
        response.setQuestionId(questionId);

        if ("on".equals(answer)) {
            response.setOptionId(Integer.parseInt(paramName.split("_")[3]));
            response.setResponseData("");
        } else {
            response.setOptionId(0);
            response.setResponseData(answer);
        }
        response.setIsValid(1);
        response.setRowId(0);
        response.setColumnId(0);
        response.setUserId(userId);
        response.setIpAddress(ipAddress);
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        responseService.updateResponse(response);
    }

    //处理矩阵题
    private void saveMatrixResponse(int surveyId, int questionId, String rowOptionId, String columnOptionId, 
                                  String ipAddress, int userId, boolean isSaveAction){
        //System.out.println("行列Id分别如下："+rowOptionId+" "+columnOptionId);

        Response response = new Response();
        response.setSurveyId(surveyId);
        response.setQuestionId(questionId);
        response.setRowId(Integer.parseInt(rowOptionId));
        response.setColumnId(Integer.parseInt(columnOptionId));
        response.setResponseData("");
        response.setOptionId(0);
        response.setIsValid(1);
        response.setUserId(userId);
        response.setIpAddress(ipAddress);
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        responseService.updateResponse(response);
    }

    //完成问卷
    private void completeSurvey(int surveyId, int userId, boolean isSaveAction){
        if (!isSaveAction) {
            userSurveyService.updateSurveyStatusBySurveyAndUser(surveyId, userId, "已完成", 
                new Timestamp(System.currentTimeMillis()));
        }else{
            userSurveyService.updateSurveyStatusBySurveyAndUser(surveyId, userId, "保存未提交",
                    new Timestamp(System.currentTimeMillis()));
        }
    }

    //打回问卷
    private void remakeSurvey(int surveyId, int userId){
        userSurveyService.updateSurveyStatusBySurveyAndUser(surveyId, userId, "保存未提交",
            new Timestamp(System.currentTimeMillis()));
    }

    //初始化答题情况
    private void initializeResponses(int surveyId, int userId, String ipAddress){
        List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);
        if (!responseService.checkResponseExists(userId, surveyId)) {
            List<Response> initialResponses = new ArrayList<>();
            
            for (Question question : questions) {
                String questionType = String.valueOf(question.getType());
                
                if ("矩阵单选".equals(questionType) || "矩阵多选".equals(questionType)) {
                    List<Option> rowOptions = optionService.getRowOptionsByQuestionId(question.getQuestionId());
                    List<Option> columnOptions = optionService.getColumnOptionsByQuestionId(question.getQuestionId());

                    for (Option row : rowOptions) {
                        for (Option column : columnOptions) {
                            Response responseRecord = createInitialResponse(surveyId, question.getQuestionId(), 
                                userId, ipAddress, row.getOptionId(), column.getOptionId());
                            initialResponses.add(responseRecord);
                        }
                    }
                } else if ("单选".equals(questionType) || "多选".equals(questionType)
                        || "评分题".equals(questionType)||"排序".equals(questionType)) {
                    List<Option> options = optionService.getOptionsByQuestionId(question.getQuestionId());
                    for (Option option : options) {
                        Response responseRecord = createInitialResponse(surveyId, question.getQuestionId(), 
                            userId, ipAddress, 0, 0);
                        responseRecord.setOptionId(option.getOptionId());
                        initialResponses.add(responseRecord);
                    }
                } else if ("填空".equals(questionType)) {
                    Response responseRecord = createInitialResponse(surveyId, question.getQuestionId(), 
                        userId, ipAddress, 0, 0);
                    initialResponses.add(responseRecord);
                }
            }
            //确保有数据时再初始化，不然数据库报错了
            if(!initialResponses.isEmpty()&&initialResponses!=null){
                responseService.saveResponses(initialResponses);
            }

        }
        else{
            // 获取所有已有文件的 responseId
            List<Response> existingFileResponses = responseService.selectExistingFileResponses(userId, surveyId);
            Set<Integer> existingResponseIds = new HashSet<>();
            for (Response response : existingFileResponses) {
                existingResponseIds.add(response.getResponseId());
            }
            if(!existingFileResponses.isEmpty()){
                // 重置 isValid，但排除已有文件的记录
                responseService.resetIsValidForResponsesExcludingIds(userId, surveyId, existingResponseIds);
            }else {
                responseService.resetIsValidForResponses(userId,surveyId);
            }
        }

    }

    //创建初始答案
    private Response createInitialResponse(int surveyId, int questionId, int userId, String ipAddress, 
                                         int rowId, int columnId) {
        Response responseRecord = new Response();
        responseRecord.setSurveyId(surveyId);
        responseRecord.setQuestionId(questionId);
        responseRecord.setUserId(userId);
        responseRecord.setIpAddress(ipAddress);
        responseRecord.setResponseData("");
        responseRecord.setIsValid(0);
        responseRecord.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        responseRecord.setRowId(rowId);
        responseRecord.setColumnId(columnId);
        return responseRecord;
    }

    // 添加处理已有文件的方法
    private void processExistingFiles(int questionId, String existingFileIds) {
        System.out.println("到处理已有文件了 existingFileIds: "+existingFileIds);
        List<Response> allFileResponses = responseService.getExistingFileResponses(questionId);
        if (existingFileIds != null && !existingFileIds.isEmpty()) {
            // 将逗号分隔的ID字符串转换为整数列表
            List<Integer> validFileIds = Arrays.stream(existingFileIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            // 获取该问题的所有文件记录
            System.out.println("validFileId为："+validFileIds);
            // 将不在validFileIds中的记录设置为无效
            System.out.print("现存文件id为： ---");
            for (Response response : allFileResponses) {
                System.out.print(" "+response.getResponseId());
                if (!validFileIds.contains(response.getResponseId())) {
                    System.out.println("给无效文件设置isValid为0");
                    response.setIsValid(0);
                    responseService.updateFileValid(response);
                }
            }
        }else{
            for (Response response : allFileResponses) {
                System.out.print(" "+response.getResponseId());
                    System.out.println("给无效文件设置isValid为0");
                    response.setIsValid(0);
                    responseService.updateFileValid(response);
            }
        }
    }
}

