package SurveySystem.Service.Impl;


import SurveySystem.Mapper.CategoryMapper;
import SurveySystem.Mapper.DepartmentSurveyMapper;
import SurveySystem.Model.DepartmentSurvey;
import SurveySystem.Service.DepartmentSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentSurveyServiceImpl implements DepartmentSurveyService {
    private final DepartmentSurveyMapper departmentSurveyMapper;

    @Autowired
    public DepartmentSurveyServiceImpl(DepartmentSurveyMapper departmentSurveyMapper) {
        this.departmentSurveyMapper = departmentSurveyMapper;
    }
    @Override
    public void assignToDepartment(int departmentId, int surveyId) {
        departmentSurveyMapper.assignToDepartment(departmentId,surveyId);
    }

    @Override
    public List<DepartmentSurvey> getDepartmentSurveys(int departmentId) {
        return departmentSurveyMapper.getDepartmentSurveys(departmentId);
    }

    @Override
    public boolean checkAssignedSurvey(int surveyId, int departmentId) {
        return departmentSurveyMapper.checkAssignedSurvey(surveyId,departmentId);
    }
}
