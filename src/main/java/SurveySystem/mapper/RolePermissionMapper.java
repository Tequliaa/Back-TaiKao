package SurveySystem.mapper;

import SurveySystem.entity.Permission;
import SurveySystem.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RolePermissionMapper {
    /**
     * 添加权限
     * @param rolePermission
     * @return
     */
    int insertRolePermission(RolePermission rolePermission);

    /**
     * 删除某条权限
     * @param id
     */
    void deleteRolePermissionById(int id);

    /**
     * 删除某角色的所有权限
     * @param roleId
     */
    void deleteRolePermissionByRoleId(int roleId);

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
}
