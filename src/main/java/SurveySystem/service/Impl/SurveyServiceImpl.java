package SurveySystem.service.Impl;

import SurveySystem.mapper.SurveyMapper;
import SurveySystem.entity.Survey;
import SurveySystem.service.SurveyService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyMapper surveyMapper;

    @Autowired
    public SurveyServiceImpl(SurveyMapper surveyMapper) {
        this.surveyMapper = surveyMapper;
    }

    @Override
    public List<Survey> getSurveysByPage(int pageNum, int pageSize,String keyword, int userId, String role) {
        int offset = (pageNum - 1) * pageSize;
        return surveyMapper.getSurveysByPage(offset, pageSize,keyword, userId, role);
    }

    @Override
    public List<Survey> getAllSurveys(int userId) {
        return surveyMapper.getAllSurveys(userId);
    }

    @Override
    public Survey getSurveyById(int surveyId) {
        return surveyMapper.getSurveyById(surveyId);
    }

    @Override
    public int getSurveyCount(int userId,String keyword, String role) {
        return surveyMapper.getSurveyCount(userId,keyword, role);
    }

    @Override
    public void createSurvey(Survey survey) {
        surveyMapper.createSurvey(survey);
    }

    @Override
    public void updateSurvey(Survey survey) {
        surveyMapper.updateSurvey(survey);
    }

    @Override
    public void deleteSurvey(int surveyId) {
        surveyMapper.deleteSurvey(surveyId);
    }
}
