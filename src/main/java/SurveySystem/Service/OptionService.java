package SurveySystem.Service;

import SurveySystem.Model.Option;

import java.util.List;

public interface OptionService {
    boolean addOption(Option option);
    boolean updateOption(Option option);
    boolean deleteOption(int optionId);
    Option getOptionById(int optionId);
    List<Option> getRowOptionsByQuestionId(int questionId);
    List<Option> getColumnOptionsByQuestionId(int questionId);
    List<Option> getOptionsWithCheckCountByQuestionId(int questionId,int departmentId);
    List<Option> getOptionsByQuestionId(int questionId);
    List<Option> getAllOptions();
    List<Option> getOptionsByPage(int page, int pageSize , int questionId);
    int getOptionCount(int questionId);
}
