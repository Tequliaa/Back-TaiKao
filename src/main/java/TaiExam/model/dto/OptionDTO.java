package TaiExam.model.dto;

import lombok.Data;

@Data
public class OptionDTO {
    private int id;
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
