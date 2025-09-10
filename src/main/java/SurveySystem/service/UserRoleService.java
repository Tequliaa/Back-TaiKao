package SurveySystem.service;

import SurveySystem.entity.Role;
import SurveySystem.entity.User;
import SurveySystem.entity.UserRole;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserRoleService {
    //新增用户-角色关系
    int insertUserRole(UserRole userRole);

    int assignRolesToUsers(List<User> users,int roleId);

    //修改用户-角色关系
    int updateUserRole(UserRole userRole);

    //分发角色给部门用户
    void addRolesToDepartment(int departmentId, int roleId);

    //删除用户-角色关系
    void deleteUserRole(UserRole userRole);

    //删除用户-角色关系
    void deleteUserRoleById(int id);

    //清空某个用户的角色
    void deleteUserRoleByUserId(int userId);

    //通过用户查找角色列表
    List<Integer> getRolesByUserId(int userId);

    //通过角色id查找用户列表
    List<User> getUsersByRoleId(int roleId);

    //根据角色id查询用户数
    int getUsersCountByRoleId(int roleId);
}
