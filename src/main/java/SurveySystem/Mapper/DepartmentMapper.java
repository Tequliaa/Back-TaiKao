package SurveySystem.Mapper;

import SurveySystem.Model.Department;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface DepartmentMapper {
    // 获取所有部门信息
    List<Department> getAllDepartmentsByUserId(int userId) throws SQLException;

    List<Department> getAllDepartments() throws SQLException;

    // 获取所有部门信息（分页支持）
    List<Department> getAllDepartmentsByPages(int userId,int offset, int pageSize, String keyword);

    // 获取部门总数
    int getDepartmentCount(int userId,String keyword);

    // 根据 ID 获取单个部门信息
    Department getDepartmentById(int id);

    // 添加新部门
    int addDepartment(Department department);

    // 更新部门信息
    boolean updateDepartment(Department department);

    // 删除部门
    boolean deleteDepartment(int id);
}
