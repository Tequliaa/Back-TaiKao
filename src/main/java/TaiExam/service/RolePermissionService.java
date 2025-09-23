package TaiExam.service;

import TaiExam.model.entity.Permission;

import java.util.List;

public interface RolePermissionService {
    /**
     * 添加权限
     * @param roleId permissionIds
     * @return
     */
    void assignRolePermission(int roleId,List<Integer> permissionIds);

    /**
     * 删除某条权限
     * @param id
     */
    void deleteRolePermissionById(int id);

    /**
     * 删除某角色的所有权限
     * @param id
     */
    void deleteRolePermissionByRoleId(int id);

    /**
     * 根据角色id获取权限
     * @param roleId
     * @return
     */
    List<Permission> getPermissionsByRoleId(int roleId);

    /**
     * 根据角色id获取权限id
     * @param roleId
     * @return
     */
    List<Long> getPermissionIdsByRoleId(int roleId);

    /**
     * 根据角色id获取权限code
     * @param roleId
     * @return
     */
    List<String> getPermissionCodesByRoleId(int roleId);

    /**
     * 根据权限id查询角色id列表
     * @param permissionId
     * @return
     */
    List<String> getRoleIdsByPermissionId(int permissionId);
}
