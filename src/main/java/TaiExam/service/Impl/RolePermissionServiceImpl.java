package TaiExam.service.Impl;

import TaiExam.model.entity.Permission;
import TaiExam.model.entity.RolePermission;
import TaiExam.mapper.RolePermissionMapper;
import TaiExam.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public void assignRolePermission(int roleId,List<Integer> permissionIds) {
        //首先将原有权限删除掉
        List<Permission> permissions = rolePermissionMapper.getPermissionsByRoleId(roleId);
        if(permissions!=null&&permissions.size()>0){
            rolePermissionMapper.deleteRolePermissionByRoleId(roleId);
        }

        // 插入新权限
        for(Integer permissionId:permissionIds){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insertRolePermission(rolePermission);
        }
    }

    @Override
    public void deleteRolePermissionById(int id) {
        rolePermissionMapper.deleteRolePermissionById(id);
    }

    @Override
    public void deleteRolePermissionByRoleId(int id) {
        rolePermissionMapper.deleteRolePermissionByRoleId(id);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(int roleId) {
        return rolePermissionMapper.getPermissionsByRoleId(roleId);
    }

    @Override
    public List<Long> getPermissionIdsByRoleId(int roleId) {
        return rolePermissionMapper.getPermissionIdsByRoleId(roleId);
    }

    @Override
    public List<String> getPermissionCodesByRoleId(int roleId) {
        return rolePermissionMapper.getPermissionCodesByRoleId(roleId);
    }

    @Override
    public List<String> getRoleIdsByPermissionId(int permissionId) {
        return rolePermissionMapper.getRoleIdsByPermissionId(permissionId);
    }
}
