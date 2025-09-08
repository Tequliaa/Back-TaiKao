package SurveySystem.entity.dto;

import SurveySystem.entity.Category;
import SurveySystem.entity.Question;
import SurveySystem.entity.Survey;
import lombok.Data;

import java.util.List;

@Data
public class SurveyDto {
    private Survey survey;
    private List<Question> questions;
    private List<Category> categories;
}
