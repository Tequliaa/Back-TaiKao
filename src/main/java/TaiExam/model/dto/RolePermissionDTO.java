package TaiExam.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionDTO {
    private int roleId;
    private List<Integer> permissionIds;
}
