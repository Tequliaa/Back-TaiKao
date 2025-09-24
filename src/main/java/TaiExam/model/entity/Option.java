package TaiExam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int questionId;
    private String questionName;
    private String type;
    private String description;
    private String sortKey;
    private int isSkip;
    private int isOpenOption;
    private int skipTo;
    private int checkCount;
    private int examId;
}
