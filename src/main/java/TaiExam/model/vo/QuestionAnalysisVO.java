package TaiExam.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class QuestionAnalysisVO {
    private String questionName;
    private String questionType;
    private List<OptionAnalysisVO> options;
}
