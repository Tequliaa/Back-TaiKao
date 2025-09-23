package TaiExam.mapper;

import TaiExam.model.entity.Exam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamMapper {
    List<Exam> getAllExams(int userId);
    List<Integer> getAllExamIds();
    Exam getExamById(int examId);
    List<Exam> getExamsByPage(int offset, int pageSize,String keyword, int userId, String role);
    int getExamCount(int userId,String keyword, String role);
    void createExam(Exam exam);
    void updateExam(Exam exam);
    void deleteExam(int examId);
}