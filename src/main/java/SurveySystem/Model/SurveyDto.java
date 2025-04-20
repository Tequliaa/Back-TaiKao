package SurveySystem.Model;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;

@Data
public class SurveyDto {
    private Survey survey;
    private List<Question> questions;
    private List<Category> categories;
}
