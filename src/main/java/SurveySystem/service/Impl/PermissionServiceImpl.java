package SurveySystem.service.Impl;

import SurveySystem.entity.Permission;
import SurveySystem.entity.Role;
import SurveySystem.mapper.PermissionMapper;
import SurveySystem.service.PermissionService;
import SurveySystem.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public int insertPermission(Permission permission) {
        return permissionMapper.insertPermission(permission);
    }

    @Override
    public Boolean deletePermissionById(int id) {
        //检查是否有其他角色在用此权限。
        List<String> roleIds = rolePermissionService.getRoleIdsByPermissionId(id);
        if(roleIds!=null&&roleIds.size()>0){
            return false;
        }
        permissionMapper.deletePermissionById(id);
        return true;
    }

    @Override
    public Permission getPermissionByCode(String code) {
        return permissionMapper.getPermissionByCode(code);
    }

    @Override
    public Permission getPermissionById(int id) {
        return permissionMapper.getPermissionById(id);
    }

    @Override
    public void updatePermission(Permission permission) {
        permissionMapper.updatePermission(permission);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.getAllPermissions();
    }

    @Override
    public List<Permission> getPermissionsByPage(int pageNum, int pageSize, String keyword) {
        int offset = (pageNum - 1) * pageSize;
        return permissionMapper.getPermissionsByPage(offset, pageSize, keyword);
    }

    @Override
    public int getPermissionCount(String keyword) {
        return permissionMapper.getPermissionCount(keyword);
    }
}
