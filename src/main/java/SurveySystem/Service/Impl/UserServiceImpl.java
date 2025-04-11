package SurveySystem.Service.Impl;

import SurveySystem.Mapper.UserMapper;
import SurveySystem.Model.User;
import SurveySystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean registerUser(User user) {
        return userMapper.registerUser(user);
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
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
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
    public List<User> getUsersByPage(int currentPage, int pageSize, String keyword, int departmentId) {
        int offset = (currentPage-1)*pageSize;
        return userMapper.getUsersByPage(offset,pageSize,keyword,departmentId);
    }

    @Override
    public int getUserCount(String keyword, int departmentId) {
        return userMapper.getUserCount(keyword,departmentId);
    }

    @Override
    public List<User> getUsersByDepartmentId(int departmentId) {
        return userMapper.getUsersByDepartmentId(departmentId);
    }
}
