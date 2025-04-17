package SurveySystem.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "department_survey")
public class DepartmentSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int departmentId;
    int surveyId;
    String departmentName;
    Timestamp assignAt;
}
