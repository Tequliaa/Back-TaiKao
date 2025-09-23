package TaiExam.mapper;

import TaiExam.model.entity.DepartmentExam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DepartmentExamMapper {
    //查询时，联立department表，给department_name赋值
    void assignToDepartment(int departmentId,int examId);

    //根据departmentId查询所有的记录
    List<DepartmentExam> getDepartmentExams(int departmentId);
    boolean checkAssignedExam(int examId,int departmentId);
}
