package TaiExam.controller;

import TaiExam.model.entity.Permission;
import TaiExam.model.entity.Result;
import TaiExam.service.PermissionService;
import TaiExam.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;

    // 获取权限列表
    @RequestMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String keyword) {
        List<Permission> permissions = permissionService.getPermissionsByPage(pageNum, pageSize,keyword);
        int totalCount = permissionService.getPermissionCount(keyword);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("permissions", permissions);
        resultMap.put("totalCount", totalCount);
        return Result.success(resultMap);
    }

    // 获取所有权限
    @RequestMapping("/getAll")
    public Result<List<Permission>> getAll() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }

    // 添加权限
    @RequestMapping("/add")
    public Result<Void> createPermission(@RequestBody Permission permission) {
        permissionService.insertPermission(permission);

        return Result.success();
    }


    // 更新权限
    @RequestMapping("/update")
    public Result<Void> updatePermission(@RequestBody Permission permission) {
        permissionService.updatePermission(permission);
        return Result.success();
    }

    //删除权限
    @RequestMapping ("/delete")
    public Result<Void> deletePermission(@RequestParam int id) {
        if(permissionService.deletePermissionById(id))
            return Result.success();
        else{
            return Result.error("删除权限失败，有角色拥有该权限，请查验。");
        }
    }


    // 获取所有权限
    @RequestMapping("/getAllPermissions")
    public Result<List<Permission>> getPermissionsByRoleId() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }

    // 根据角色id获取权限码集合
    @RequestMapping("/getPermissionCodesByRoleId")
    public Result<List<String>> getPermissionCodesByRoleId(@RequestParam int roleId) {
        List<String> permissionCodes = rolePermissionService.getPermissionCodesByRoleId(roleId);
        return Result.success(permissionCodes);
    }

    // 根据角色id获取权限集合
    @RequestMapping("/getPermissionsByRoleId")
    public Result<List<Permission>> getPermissionIdsByRoleId(@RequestParam int roleId) {
        List<Permission> permissions = rolePermissionService.getPermissionsByRoleId(roleId);
        return Result.success(permissions);
    }
}
