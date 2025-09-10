package SurveySystem.mapper;

import SurveySystem.entity.Role;
import SurveySystem.entity.User;
import SurveySystem.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    //新增用户-角色关系
    int insertUserRole(UserRole userRole);

    //批量为用户新增角色
    int assignRolesToUsers(List<User> users, int roleId);

    //删除用户-角色关系
    void deleteUserRole(UserRole userRole);

    //删除用户-角色关系
    void deleteUserRoleById(int id);

    //清空某个用户的角色
    void deleteUserRoleByUserId(int userId);

    //通过用户查找角色列表
    List<Integer> getRoleIdsByUserId(int userId);

    //通过角色id查找用户列表
    List<User> getUsersByRoleId(int roleId);

    //为对应部门下的用户赋角色。
    void addRolesToDepartment(List<User> users,int roleId);

    //根据角色id查询用户数
    int getUsersCountByRoleId(int roleId);
}
