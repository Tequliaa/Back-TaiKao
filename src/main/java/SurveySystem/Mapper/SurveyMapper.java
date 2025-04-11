package SurveySystem.Mapper;

import org.apache.ibatis.annotations.Mapper;
import SurveySystem.Model.Survey;
import java.util.List;

@Mapper
public interface SurveyMapper {
    List<Survey> getAllSurveys(int userId);
    Survey getSurveyById(int surveyId);
    List<Survey> getSurveysByPage(int offset, int pageSize,String keyword, int userId, String role);
    int getSurveyCount(int userId,String keyword, String role);
    void createSurvey(Survey survey);
    void updateSurvey(Survey survey);
    void deleteSurvey(int surveyId);
}