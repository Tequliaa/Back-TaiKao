package SurveySystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Entity
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int responseId;
    private int surveyId;
    private int questionId;
    private int optionId;
    private int rowId;
    private int columnId;
    private int userId;
    private String userName;
    private String ipAddress;
    private String responseData;
    private Timestamp createdAt;
    private int totalQuestions;
    private int isValid;
    private String status;
    private String filePath;
    private int sortOrder;
}
