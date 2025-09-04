package SurveySystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "admindepartmentmapping")
public class AdminDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private int userId;
    private int departmentId;
    private Timestamp createdAt;
}
