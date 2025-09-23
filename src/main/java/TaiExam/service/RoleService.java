package TaiExam.service;

import TaiExam.model.entity.Role;

import java.util.List;

public interface RoleService {
    int insertRole(Role role);

    void deleteRoleById(int id);

    Role getRoleByName(String roleName);

    Role getRoleById(int id);

    boolean updateRole(Role role); // 更新角色信息
    List<Role> getAllRoles();
    // 获取分页角色列表
    List<Role> getRolesByPage(int offset, int pageSize, String keyword);
    // 获取角色总数
    int getRoleCount(String keyword);
}
