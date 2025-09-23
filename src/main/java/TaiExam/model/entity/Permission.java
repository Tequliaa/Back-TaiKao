package TaiExam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String permissionCode;
    private String comment;
    private Timestamp createdTime;
}
