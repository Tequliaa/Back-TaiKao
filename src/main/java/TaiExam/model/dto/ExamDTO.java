package TaiExam.model.dto;

import TaiExam.model.entity.Category;
import TaiExam.model.entity.Exam;
import TaiExam.model.entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class ExamDTO {
    private Exam exam;
    private List<Question> questions;
    private List<Category> categories;
}
