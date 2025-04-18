package SurveySystem.Model;

import jakarta.persistence.*;
import lombok.Data;
import SurveySystem.Model.Option;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;
    private int categoryId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id") // 这会在Option表中创建外键
    private List<Option> options = new ArrayList<>();
    private String CategoryName;
    private int surveyId;
    private String surveyName;
    private String description;
    private String type;
    private String displayType;
    private Timestamp createdAt;
    private Integer updatedBy;
    private Timestamp updatedAt;
    private Integer skipTo;
    private int isSkip;
    private int isRequired;
    private int isOpen;
    private int maxSelections;
    private int minSelections;
}
