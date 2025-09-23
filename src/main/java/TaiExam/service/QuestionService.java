package TaiExam.service;

import TaiExam.model.entity.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions(int examId,int userId);

    Question getQuestionById(int questionId);
    List<Question> getQuestionsByExamId(int examId);

    int addQuestionAndReturnId(Question question);

    boolean updateQuestion(Question question);

    boolean deleteQuestion(int questionId);

    List<Question> getQuestionsByPage(int page, int pageSize , int examId , int categoryId ,String keyword,int userId);
    int getQuestionCount(int examId,int categoryId,String keyword,int userId);
}
