package TaiExam.model.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ExamSubmitMessage {
    private int id;
    private boolean isSaveAction;
    private Integer answerUserId;
    private Map<String, String> formData;
    private Map<String, String> fileMap;
    private int userId;
    private String userRole;
    private String ipAddress;
    private String actionType;
}
