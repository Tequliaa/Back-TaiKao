package TaiExam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Entity
@Table(name = "user_exam")
public class UserExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int examId;
    private int departmentId;
    private String username;
    private String departmentName;
    private String examName;
    private String examDescription;
    private String status; // "未完成" or "已完成"
    private int allowView;
    private Timestamp assignedAt;
    private Timestamp completedAt;
}
