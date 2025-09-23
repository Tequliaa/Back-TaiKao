package TaiExam.mapper;

import TaiExam.model.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    int insertPermission(Permission permission);

    void deletePermissionById(int id);

    Permission getPermissionByCode(String code);

    Permission getPermissionById(int id);

    void updatePermission(Permission permission); // 更新权限信息
    List<Permission> getAllPermissions();

    // 获取分页权限列表
    List<Permission> getPermissionsByPage(int offset, int pageSize, String keyword);
    // 获取角色总数
    int getPermissionCount(String keyword);
}
