package SurveySystem.Mapper;

import SurveySystem.Model.Option;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OptionMapper {
    boolean addOption(Option option);
    boolean updateOption(Option option);
    boolean deleteOption(int optionId);
    Option getOptionById(int optionId);
    List<Option> getRowOptionsByQuestionId(int questionId);
    List<Option> getColumnOptionsByQuestionId(int questionId);
    List<Option> getOptionsWithCheckCountByQuestionId(int questionId,int departmentId);
    List<Map<String, Object>> getMatrixCellCheckCount(int questionId,int departmentId);
    List<Option> getOptionsByQuestionId(int questionId);
    List<Option> getAllOptions();
    List<Option> getOptionsByPage(int offset, int pageSize , int questionId);
    int getOptionCount(int questionId);
}
