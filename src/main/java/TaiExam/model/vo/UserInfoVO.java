package TaiExam.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {
    private int id;
    private String name;
    private String username;
    private int departmentId;
    private String departmentName;
    private String roleName;
    private List<String> permissionCodes;
}
