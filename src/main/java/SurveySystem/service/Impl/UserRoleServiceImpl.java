package SurveySystem.service.Impl;

import SurveySystem.entity.Role;
import SurveySystem.entity.User;
import SurveySystem.entity.UserRole;
import SurveySystem.mapper.UserMapper;
import SurveySystem.mapper.UserRoleMapper;
import SurveySystem.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int insertUserRole(UserRole userRole) {
        return userRoleMapper.insertUserRole(userRole);
    }

    @Override
    public int updateUserRole(UserRole userRole) {
        deleteUserRoleByUserId(userRole.getUserId());
        return userRoleMapper.insertUserRole(userRole);
    }

    @Override
    public void addRolesToDepartment(int departmentId, int roleId) {
        List<User> users = userMapper.getUsersByDepartmentId(departmentId);
        userRoleMapper.addRolesToDepartment(users,roleId);
    }

    @Override
    public void deleteUserRole(UserRole userRole) {
        userRoleMapper.deleteUserRole(userRole);
    }

    @Override
    public void deleteUserRoleById(int id) {
        userRoleMapper.deleteUserRoleById(id);
    }

    @Override
    public void deleteUserRoleByUserId(int userId) {
        userRoleMapper.deleteUserRoleByUserId(userId);
    }

    @Override
    public List<Integer> getRolesByUserId(int userId) {
        return userRoleMapper.getRoleIdsByUserId(userId);
    }

    @Override
    public List<User> getUsersByRoleId(int roleId) {
        return userRoleMapper.getUsersByRoleId(roleId);
    }

    @Override
    public int getUsersCountByRoleId(int roleId) {
        return userRoleMapper.getUsersCountByRoleId(roleId);
    }
}
