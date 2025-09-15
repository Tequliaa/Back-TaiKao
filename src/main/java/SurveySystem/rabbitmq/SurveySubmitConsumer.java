package SurveySystem.rabbitmq;

import SurveySystem.config.SurveyMessageQueueConfig;
import SurveySystem.entity.*;
import SurveySystem.handler.SurveyWebSocketHandler;
import SurveySystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SurveySubmitConsumer {
    
    @Autowired
    private UserSurveyService userSurveyService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SurveyMessageProducer surveyMessageProducer;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private OptionService optionService;
    
    @RabbitListener(queues = SurveyMessageQueueConfig.SURVEY_SUBMIT_QUEUE)
    public void handleSurveySubmit(SurveySubmitMessage message) {
        try {
            // 处理重新提交的情况
            if ("remake".equals(message.getActionType()) && "超级管理员".equals(message.getUserRole())) {
                if (message.getAnswerUserId() != null) {
                    remakeSurvey(message.getSurveyId(), message.getAnswerUserId());
                    return;
                }
                // 可以记录错误日志
                return;
            }
            
            // 检查是否已提交
            UserSurvey userSurvey = userSurveyService.getUserSurveyByUserIdAndSurveyId(
                message.getUserId(), message.getSurveyId());
            if (userSurvey!=null&&"已完成".equals(userSurvey.getStatus())) {
                // 可以记录已提交的情况
                return;
            }
            
            // 初始化响应记录
            initializeResponses(message.getSurveyId(), message.getUserId(), message.getIpAddress());
            
            // 处理表单数据
            if (message.getFormData() != null && !message.getFormData().isEmpty()) {
                processFormData(message.getFormData(), message.getSurveyId(), 
                    message.getUserId(), message.getIpAddress(), message.isSaveAction());
            }
            
            // 处理文件上传
            if (message.getFileMap() != null && !message.getFileMap().isEmpty()) {
                handleFileUploads(message.getFileMap(), message.getSurveyId(), 
                    message.getUserId(), message.getIpAddress());
            }
            
            // 完成问卷
            completeSurvey(message.getSurveyId(), message.getUserId(), message.isSaveAction());
            
            // 准备通知数据
            User user = userService.getUserByUserId(message.getUserId());
            int newUnfinishedCount = userSurveyService.getUserInfoCount(
                message.getSurveyId(), user.getDepartmentId());

            // 准备通知数据（添加 surveyId 到消息体中）
            Map<String, Object> pushData = new HashMap<>();
            pushData.put("surveyId", message.getSurveyId()); // 新增：将 surveyId 放入消息体
            pushData.put("unfinishedTotalRecords", newUnfinishedCount);
            pushData.put("message", "用户【" + user.getName() + "】已完成答题");

            // 发送通知消息（只需传递 Map，无需额外传递 surveyId）
            surveyMessageProducer.sendSurveyNotifyMessage(pushData);
            
        } catch (Exception e) {
            // 处理异常，可以考虑将消息发送到死信队列
             log.error("处理问卷提交消息失败", e);
        }
    }

    //打回问卷
    private void remakeSurvey(int surveyId, int userId){
        userSurveyService.updateSurveyStatusBySurveyAndUser(surveyId, userId, "保存未提交",
                new Timestamp(System.currentTimeMillis()));
        User user = userService.getUserByUserId(userId);
        int newUnfinishedCount = userSurveyService.getUserInfoCount(
                surveyId, user.getDepartmentId());
        // 准备通知数据（添加 surveyId 到消息体中）
        Map<String, Object> pushData = new HashMap<>();
        pushData.put("surveyId", surveyId); // 新增：将 surveyId 放入消息体
        pushData.put("unfinishedTotalRecords", newUnfinishedCount);
        pushData.put("message", "用户【" + user.getName() + "】问卷被打回");
        // 处理WebSocket广播
        SurveyWebSocketHandler.broadcastToSurvey(surveyId, pushData);
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

    // 处理表单数据
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

    // 处理文件上传 - 通过文件路径获取文件
    private void handleFileUploads(Map<String, String> fileInfoMap, int surveyId, int userId, String ipAddress) throws IOException {
        System.out.println("有新文件上传，通过路径处理");

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

        for (Map.Entry<String, String> entry : fileInfoMap.entrySet()) {
            String paramName = entry.getKey();
            String fileInfo = entry.getValue();

            // 解析文件信息（格式：临时文件路径|原始文件名|内容类型）
            String[] infoParts = fileInfo.split("\\|");
            if (infoParts.length < 3) {
                System.err.println("无效的文件信息格式: " + fileInfo);
                continue;
            }

            String tempFilePath = infoParts[0];  // 临时文件路径
            String originalFilename = infoParts[1];  // 原始文件名
            String contentType = infoParts[2];  // 文件类型

            // 从参数名中提取问题ID
            int questionId;
            try {
                questionId = Integer.parseInt(paramName.split("_")[1]);
            } catch (Exception e) {
                System.err.println("解析问题ID失败: " + paramName);
                continue;
            }

            // 获取文件扩展名并验证
            String fileExtension;
            try {
                fileExtension = originalFilename
                        .substring(originalFilename.lastIndexOf(".") + 1)
                        .toLowerCase();
            } catch (Exception e) {
                System.err.println("解析文件扩展名失败: " + originalFilename);
                continue;
            }

            if (!allowedFileTypes.contains(fileExtension)) {
                throw new IllegalArgumentException("不支持的文件类型: " + fileExtension);
            }

            // 验证临时文件是否存在
            File tempFile = new File(tempFilePath);
            if (!tempFile.exists() || !tempFile.isFile()) {
                throw new FileNotFoundException("临时文件不存在: " + tempFilePath);
            }

            // 验证文件大小
            if (tempFile.length() > 20 * 1024 * 1024) {
                throw new IllegalArgumentException("文件过大，请上传小于 20MB 的文件");
            }

            // 生成目标文件名并复制文件
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            File destFile = new File(uploadDir, fileName);

            try {
                // 复制临时文件到目标目录
                Files.copy(tempFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // 记录文件路径 - 使用WebConfig中配置的映射路径
                String filePath = "/uploads/" + fileName;
                System.out.println("文件从临时路径[" + tempFilePath + "]复制到: " + destFile.getAbsolutePath());
                System.out.println("访问路径: " + filePath);

                // 保存文件信息到数据库
                Response response = new Response();
                response.setSurveyId(surveyId);
                response.setQuestionId(questionId);
                response.setUserId(userId);
                response.setIsValid(1);
                response.setIpAddress(ipAddress);
                response.setFilePath(filePath);
                responseService.saveFilePathToDatabase(response);

                // 清理临时文件
                if (!tempFile.delete()) {
                    System.err.println("临时文件清理失败: " + tempFilePath);
                }

            } catch (IOException e) {
                System.err.println("文件复制失败: " + e.getMessage());
                throw e;
            }
        }
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

    //处理单选多选和填空题
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

    //处理评分题
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

    //处理开放题
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
