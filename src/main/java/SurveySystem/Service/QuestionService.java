package SurveySystem.Service;

import SurveySystem.Model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions(int surveyId,int userId);

    Question getQuestionById(int questionId);
    List<Question> getQuestionsBySurveyId(int surveyId);

    int addQuestionAndReturnId(Question question);

    boolean updateQuestion(Question question);

    boolean deleteQuestion(int questionId);

    List<Question> getQuestionsByPage(int page, int pageSize , int surveyId , int categoryId ,String keyword,int userId);
    int getQuestionCount(int surveyId,int categoryId,String keyword,int userId);
}
