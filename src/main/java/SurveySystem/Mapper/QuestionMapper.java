package SurveySystem.Mapper;

import SurveySystem.Model.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<Question> getAllQuestions(int surveyId,int userId);

    Question getQuestionById(int questionId);
    List<Question> getQuestionsBySurveyId(int surveyId);

    int addQuestionAndReturnId(Question question);

    boolean updateQuestion(Question question);

    boolean deleteQuestion(int questionId);

    List<Question> getQuestionsByPage(int offset, int pageSize , int surveyId , int categoryId ,String keyword,int userId);
    int getQuestionCount(int surveyId,int categoryId,String keyword,int userId);
}
