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
    List<Map<String, Object>> getSortStatistics(int questionId, int departmentId);



    /**
     * 获取排序题每个位置的出现频率
     * @param questionId 问题ID
     * @param departmentId 部门ID
     * @return 位置频率统计数据列表
     */
    List<Map<String, Object>> getSortPositionFrequencies(int questionId, int departmentId);
    List<Option> getOptionsByQuestionId(int questionId);
    List<Option> getAllOptions(int userId);
    List<Option> getOptionsByPage(int offset, int pageSize , int questionId,int userId);
    int getOptionCount(int questionId,int userId);
}
