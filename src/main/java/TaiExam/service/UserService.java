package TaiExam.service;

import TaiExam.model.entity.User;

import java.util.List;

public interface UserService {
    int registerUser(User user); // 注册用户
    // UserMapper.java
    int batchInsertUsers(List<User> userList);
    User getUserByUserId(int id);
    User getUserByUsername(String username); // 通过用户名获取用户
    boolean updatePassword(User user); //重置用户密码
    void deleteUserById(int id);
    boolean updateUser(User user); // 更新用户信息
    // 获取分页用户列表
    List<User> getUsersByPage(int currentPage, int pageSize, String keyword, int departmentId,int userId);
    // 获取用户总数
    int getUserCount(String keyword,int departmentId,int userId);
    List<User> getUsersByDepartmentId(int departmentId); //根据部门ID获取用户信息

    long getUsersByRoleId(int roleId);
}
