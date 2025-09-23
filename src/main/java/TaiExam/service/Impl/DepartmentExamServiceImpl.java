package TaiExam.service.Impl;


import TaiExam.mapper.DepartmentExamMapper;
import TaiExam.model.entity.DepartmentExam;
import TaiExam.service.DepartmentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentExamServiceImpl implements DepartmentExamService {
    private final DepartmentExamMapper departmentExamMapper;

    @Autowired
    public DepartmentExamServiceImpl(DepartmentExamMapper departmentExamMapper) {
        this.departmentExamMapper = departmentExamMapper;
    }
    @Override
    public void assignToDepartment(int departmentId, int examId) {
        departmentExamMapper.assignToDepartment(departmentId,examId);
    }

    @Override
    public List<DepartmentExam> getDepartmentExams(int departmentId) {
        return departmentExamMapper.getDepartmentExams(departmentId);
    }

    @Override
    public boolean checkAssignedExam(int examId, int departmentId) {
        return departmentExamMapper.checkAssignedExam(examId,departmentId);
    }
}
