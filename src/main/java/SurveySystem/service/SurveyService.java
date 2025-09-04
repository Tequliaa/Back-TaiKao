package SurveySystem.service;

import SurveySystem.entity.Survey;

import java.util.List;
public interface SurveyService {
        List<Survey> getSurveysByPage(int page, int pageSize,String keyword, int userId, String role);
        List<Survey> getAllSurveys(int userId);
        Survey getSurveyById(int surveyId);
        int getSurveyCount(int userId,String keyword, String role);
        void createSurvey(Survey survey);
        void updateSurvey(Survey survey);
        void deleteSurvey(int surveyId);
    }
