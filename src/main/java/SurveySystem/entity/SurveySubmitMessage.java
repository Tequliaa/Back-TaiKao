package SurveySystem.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Data
public class SurveySubmitMessage {
    private int surveyId;
    private boolean isSaveAction;
    private Integer answerUserId;
    private Map<String, String> formData;
    private Map<String, String> fileMap;
    private int userId;
    private String userRole;
    private String ipAddress;
    private String actionType;
}
