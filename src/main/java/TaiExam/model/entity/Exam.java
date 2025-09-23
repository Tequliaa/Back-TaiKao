package TaiExam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private int createdBy;
    private String createdByName;
    private int allowView;
    private Timestamp createdAt;
    private Integer updatedBy;
    private String updatedByName;
    private Timestamp updatedAt;
    private String status;
    //问卷内问题 是否按分类排序
    private int isCategory;
}
