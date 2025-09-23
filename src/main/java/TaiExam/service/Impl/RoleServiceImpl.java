package TaiExam.service.Impl;

import TaiExam.model.entity.Role;
import TaiExam.mapper.RoleMapper;
import TaiExam.service.RoleService;
import TaiExam.service.UserRoleService;
import TaiExam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Override
    public int insertRole(Role role) {
        return roleMapper.insertRole(role);
    }

    @Override
    public void deleteRoleById(int id) {
        long count = userRoleService.getUsersCountByRoleId(id);
        if(count>0){
            throw new RuntimeException("该角色下有用户，请先删除用户.");
        }
        roleMapper.deleteRoleById(id);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleMapper.getRoleByName(roleName);
    }

    @Override
    public Role getRoleById(int id) {
        return roleMapper.getRoleById(id);
    }

    @Override
    public boolean updateRole(Role role) {
        return roleMapper.updateRole(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.getAllRoles();
    }

    @Override
    public List<Role> getRolesByPage(int pageNum, int pageSize, String keyword) {
        int offset = (pageNum - 1) * pageSize;
        return roleMapper.getRolesByPage(offset,pageSize,keyword);
    }

    @Override
    public int getRoleCount(String keyword) {
        return roleMapper.getRoleCount(keyword);
    }
}
