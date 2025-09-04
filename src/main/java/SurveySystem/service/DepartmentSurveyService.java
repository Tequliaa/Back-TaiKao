package SurveySystem.service;

import SurveySystem.entity.DepartmentSurvey;

import java.util.List;

public interface DepartmentSurveyService {
    //查询时，联立department表，给department_name赋值
    void assignToDepartment(int departmentId,int surveyId);

    //根据departmentId查询所有的记录
    List<DepartmentSurvey> getDepartmentSurveys(int departmentId);
    boolean checkAssignedSurvey(int surveyId,int departmentId);

}
