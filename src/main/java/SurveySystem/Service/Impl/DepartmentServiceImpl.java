package SurveySystem.Service.Impl;

import SurveySystem.Mapper.DepartmentMapper;
import SurveySystem.Model.Department;
import SurveySystem.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }
    @Override
    public List<Department> getAllDepartmentsByUserId(int userId) throws SQLException {
        return departmentMapper.getAllDepartmentsByUserId(userId);
    }

    @Override
    public List<Department> getAllDepartments() throws SQLException {
        return departmentMapper.getAllDepartments();
    }

    @Override
    public List<Department> getAllDepartmentsByPages(int userId, int currentPage, int pageSize, String keyword) {
        int offset = (currentPage - 1) * pageSize;
        return departmentMapper.getAllDepartmentsByPages(userId,offset,pageSize,keyword);
    }

    @Override
    public int getDepartmentCount(int userId, String keyword) {
        return departmentMapper.getDepartmentCount(userId,keyword);
    }

    @Override
    public Department getDepartmentById(int id) {
        return departmentMapper.getDepartmentById(id);
    }

    @Override
    public Department getDepartmentByName(String departmentName) {
        return departmentMapper.getDepartmentByName(departmentName);
    }

    @Override
    public int addDepartment(Department department) {
        return departmentMapper.addDepartment(department);
    }

    @Override
    public boolean updateDepartment(Department department) {
        return departmentMapper.updateDepartment(department);
    }

    @Override
    public boolean deleteDepartment(int id) {
        return departmentMapper.deleteDepartment(id);
    }
}
