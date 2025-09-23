package TaiExam.mapper;

import TaiExam.model.entity.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<Question> getAllQuestions(int examId,int userId);

    Question getQuestionById(int questionId);
    List<Question> getQuestionsByExamId(int examId);

    int addQuestionAndReturnId(Question question);

    boolean updateQuestion(Question question);

    boolean deleteQuestion(int questionId);

    List<Question> getQuestionsByPage(int offset, int pageSize , int examId , int categoryId ,String keyword,int userId);
    int getQuestionCount(int examId,int categoryId,String keyword,int userId);
}
