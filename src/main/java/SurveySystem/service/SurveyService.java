package SurveySystem.service;

import SurveySystem.entity.Survey;
import org.springframework.data.redis.core.HashOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface SurveyService {
        List<Survey> getSurveysByPage(int page, int pageSize,String keyword, int userId, String role);
        List<Survey> getAllSurveys(int userId);
        List<Integer> getAllSurveyIds();
        Survey getSurveyById(int surveyId);
        int getSurveyCount(int userId,String keyword, String role);
        void createSurvey(Survey survey);
        void updateSurvey(Survey survey);
        void deleteSurvey(int surveyId);
    }
