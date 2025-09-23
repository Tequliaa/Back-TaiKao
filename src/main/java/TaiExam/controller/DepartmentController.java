package TaiExam.controller;

import TaiExam.mapper.AdminDepartmentMapper;
import TaiExam.model.entity.Department;
import TaiExam.model.entity.Result;
import TaiExam.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final AdminDepartmentMapper adminDepartmentMapper;

    @Autowired
    public DepartmentController(DepartmentService departmentService,AdminDepartmentMapper adminDepartmentMapper) {
        this.departmentService = departmentService;
        this.adminDepartmentMapper = adminDepartmentMapper;
    }


    /**
     * 分页获取所有部门列表
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param userId
     * @return
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> listDepartments(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int userId) {
        List<Department> departments = departmentService.getAllDepartmentsByPages(userId,pageNum, pageSize,keyword);
        int totalCount = departmentService.getDepartmentCount(userId, keyword);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("departments", departments);
        resultMap.put("totalCount", totalCount);
        return Result.success(resultMap);
    }

    /**
     * 获取所有部门列表
     * @param userId
     * @return
     * @throws SQLException
     */
    @GetMapping("/getAllById")
    public Result<List<Department>> getAllDepartmentsById(@RequestParam int userId) throws SQLException {
        System.out.println("请求到这里了");
        List<Department> departments = departmentService.getAllDepartmentsByUserId(userId);
        return Result.success(departments);
    }


    /**
     * 创建部门
     * @param department
     * @param userId
     * @return
     */
    @PostMapping("/add")
    public Result<Void> createExam(@RequestBody Department department,@RequestParam int userId) {
        departmentService.addDepartment(department);
        int departmentId=department.getId();
        if(userId!=1){
            adminDepartmentMapper.addMapping(1,departmentId);
        }
        adminDepartmentMapper.addMapping(userId,departmentId);
        return Result.success();
    }

    /**
     * 更新部门信息
     * @param department
     * @return
     */
    @PutMapping("/update")
    public Result<Void> updateDepartment(@RequestBody Department department) {
        departmentService.updateDepartment(department);
        return Result.success();
    }

    /**
     *
     * @param departmentId
     * @return
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteDepartment(@RequestParam int departmentId) {
        departmentService.deleteDepartment(departmentId);
        return Result.success();
    }
}

