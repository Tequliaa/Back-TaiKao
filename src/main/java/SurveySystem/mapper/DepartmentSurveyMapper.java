package SurveySystem.mapper;

import SurveySystem.entity.DepartmentSurvey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DepartmentSurveyMapper {
    //查询时，联立department表，给department_name赋值
    void assignToDepartment(int departmentId,int surveyId);

    //根据departmentId查询所有的记录
    List<DepartmentSurvey> getDepartmentSurveys(int departmentId);
    boolean checkAssignedSurvey(int surveyId,int departmentId);
}
