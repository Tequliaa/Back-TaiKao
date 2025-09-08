package SurveySystem.service.Impl;

import SurveySystem.mapper.UserMapper;
import SurveySystem.entity.User;
import SurveySystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int registerUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public int batchInsertUsers(List<User> userList) {
        // 过滤掉已存在的用户名
        List<User> newUsers = userList.stream()
                .filter(user -> getUserByUsername(user.getUsername()) == null)
                .collect(Collectors.toList());

        if (!newUsers.isEmpty()) {
            return userMapper.batchInsertUsers(newUsers);
        }
        return 0;
    }

    @Override
    public User getUserByUserId(int id) {
        return userMapper.getUserByUserId(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public boolean updatePassword(User user) {
        return userMapper.updatePassword(user);
    }

    @Override
    public void deleteUserById(int id) {
        userMapper.deleteUserById(id);
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public List<User> getUsersByPage(int currentPage, int pageSize, String keyword,int departmentId, int userId) {
        int offset = (currentPage-1)*pageSize;
        return userMapper.getUsersByPage(offset,pageSize,keyword,departmentId,userId);
    }

    @Override
    public int getUserCount(String keyword, int departmentId,int userId) {
        return userMapper.getUserCount(keyword,departmentId,userId);
    }

    @Override
    public List<User> getUsersByDepartmentId(int departmentId) {
        return userMapper.getUsersByDepartmentId(departmentId);
    }

    @Override
    public long getUsersByRoleId(int roleId) {
        return userMapper.getUsersByRoleId(roleId);
    }
}