package TaiExam.service;

import TaiExam.model.entity.DepartmentExam;

import java.util.List;

public interface DepartmentExamService {
    //查询时，联立department表，给department_name赋值
    void assignToDepartment(int departmentId,int examId);

    //根据departmentId查询所有的记录
    List<DepartmentExam> getDepartmentExams(int departmentId);
    boolean checkAssignedExam(int examId,int departmentId);

}
