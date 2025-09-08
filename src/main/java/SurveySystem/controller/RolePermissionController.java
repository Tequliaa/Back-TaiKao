package SurveySystem.controller;

import SurveySystem.entity.Result;
import SurveySystem.entity.dto.RolePermissionDTO;
import SurveySystem.service.PermissionService;
import SurveySystem.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rolePermission")
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;

    //删除权限
    @RequestMapping ("/delete")
    public Result<Void> deleteRole(@RequestParam int id) {
        rolePermissionService.deleteRolePermissionById(id);
        return Result.success();
    }

    //给角色分配权限
    @RequestMapping ("/assign")
    public Result<Void> assignPermissionToRole(@RequestBody RolePermissionDTO rolePermissionDTO) {
        rolePermissionService.assignRolePermission(rolePermissionDTO.getRoleId(),
                rolePermissionDTO.getPermissionIds());
        return Result.success();
    }
}
