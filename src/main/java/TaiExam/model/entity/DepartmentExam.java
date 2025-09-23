package TaiExam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "department_exam")
public class DepartmentExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int departmentId;
    int examId;
    String departmentName;
    Timestamp assignAt;
}
