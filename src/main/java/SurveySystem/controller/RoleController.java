package SurveySystem.controller;

import SurveySystem.entity.*;
import SurveySystem.service.RoleService;
import SurveySystem.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    // 获取角色列表
    @RequestMapping("/list")
    public Result<Map<String, Object>> listRoles(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String keyword) {
        List<Role> roles = roleService.getRolesByPage(pageNum, pageSize,keyword);
        int totalCount = roleService.getRoleCount(keyword);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("roles", roles);
        resultMap.put("totalCount", totalCount);
        return Result.success(resultMap);
    }

    // 获取所有角色
    @RequestMapping("/getAll")
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    //新增角色
    @RequestMapping("/add")
    public Result<Void> createRole(@RequestBody Role role) {
        roleService.insertRole(role);

        return Result.success();
    }

    //更新角色
    @RequestMapping("/update")
    public Result<Void> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success();
    }

    //删除角色
    @RequestMapping ("/delete")
    public Result<Void> deleteRole(@RequestParam int roleId) {
        roleService.deleteRoleById(roleId);
        return Result.success();
    }

    //分发角色给部门用户
    @RequestMapping ("/assignRoleToDepartment")
    public Result<Void> addRolesToDepartment(@RequestParam int departmentId, @RequestParam int roleId) {
        userRoleService.addRolesToDepartment(departmentId,roleId);
        return Result.success();
    }






}
