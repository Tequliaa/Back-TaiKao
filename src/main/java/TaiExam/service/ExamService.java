package TaiExam.service;

import TaiExam.model.entity.Exam;

import java.util.List;

public interface ExamService {
        List<Exam> getExamsByPage(int page, int pageSize,String keyword, int userId, String role);
        List<Exam> getAllExams(int userId);
        List<Integer> getAllExamIds();
        Exam getExamById(int examId);
        int getExamCount(int userId,String keyword, String role);
        void createExam(Exam exam);
        void updateExam(Exam exam);
        void deleteExam(int id);
    }
