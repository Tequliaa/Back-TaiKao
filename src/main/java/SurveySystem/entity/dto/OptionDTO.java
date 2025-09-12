package SurveySystem.entity.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class OptionDTO {
    private int optionId;
    private int questionId;
    private String questionName;
    private String type;
    private String description;
    private String sortKey;
    private int isSkip;
    private int isOpenOption;
    private int skipTo;
    private int checkCount;

}
