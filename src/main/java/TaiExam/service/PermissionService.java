package TaiExam.service;

import TaiExam.model.entity.Permission;

import java.util.List;

public interface PermissionService {
    int insertPermission(Permission permission);

    Boolean deletePermissionById(int id);

    Permission getPermissionByCode(String code);

    Permission getPermissionById(int id);

    void updatePermission(Permission permission); // 更新权限信息
    List<Permission> getAllPermissions();

    // 获取分页权限列表
    List<Permission> getPermissionsByPage(int pageNum, int pageSize, String keyword);
    // 获取角色总数
    int getPermissionCount(String keyword);
}
