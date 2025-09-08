package SurveySystem.entity.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String name;
    private String username;
    private String password;
    private int departmentId;
    private String departmentName;
    private String salt;
    private String role;

}
