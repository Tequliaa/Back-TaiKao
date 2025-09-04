package SurveySystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Entity
@Table(name = "user_survey")
public class UserSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int surveyId;
    private int departmentId;
    private String username;
    private String departmentName;
    private String surveyName;
    private String surveyDescription;
    private String status; // "未完成" or "已完成"
    private int allowView;
    private Timestamp assignedAt;
    private Timestamp completedAt;
}
