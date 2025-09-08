package SurveySystem.mapper;

import SurveySystem.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserMapper {
    int insertUser(User user); // 注册用户
    int batchInsertUsers(List<User> userList);
    User getUserByUserId(int id);
    User getUserByUsername(String username); // 通过用户名获取用户
    boolean updatePassword(User user); //重置用户密码
    void deleteUserById(int id);
    boolean updateUser(User user); // 更新用户信息
    // 获取分页用户列表
    List<User> getUsersByPage(int offset, int pageSize, String keyword, int departmentId,int userId);
    // 获取用户总数
    int getUserCount(String keyword,int departmentId,int userId);
    List<User> getUsersByDepartmentId(int departmentId); //根据部门ID获取用户信息

    // 查看某角色下是否有用户
    Long getUsersByRoleId(int roleId);
}
