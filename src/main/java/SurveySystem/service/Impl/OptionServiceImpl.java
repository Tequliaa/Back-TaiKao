package SurveySystem.service.Impl;

import SurveySystem.mapper.OptionMapper;
import SurveySystem.entity.Option;
import SurveySystem.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements OptionService {
    private final OptionMapper optionMapper;

    @Autowired
    public OptionServiceImpl(OptionMapper optionMapper) {
        this.optionMapper = optionMapper;
    }
    @Override
    public boolean addOption(Option option) {
        return optionMapper.addOption(option);
    }

    @Override
    public boolean updateOption(Option option) {
        return optionMapper.updateOption(option);
    }

    @Override
    public boolean deleteOption(int optionId) {
        return optionMapper.deleteOption(optionId);
    }

    @Override
    public Option getOptionById(int optionId) {
        return optionMapper.getOptionById(optionId);
    }

    @Override
    public List<Option> getRowOptionsByQuestionId(int questionId) {
        return optionMapper.getRowOptionsByQuestionId(questionId);
    }

    @Override
    public List<Option> getColumnOptionsByQuestionId(int questionId) {
        return optionMapper.getColumnOptionsByQuestionId(questionId);
    }

    @Override
    public List<Option> getOptionsWithCheckCountByQuestionId(int questionId, int departmentId) {
        return optionMapper.getOptionsWithCheckCountByQuestionId(questionId,departmentId);
    }

    @Override
    public List<Map<String, Object>> getMatrixCellCheckCount(int questionId, int departmentId) {
        return optionMapper.getMatrixCellCheckCount(questionId,departmentId);
    }

    @Override
    public List<Map<String, Object>> getSortStatistics(int questionId, int departmentId) {
        return optionMapper.getSortStatistics(questionId, departmentId);
    }

    @Override
    public List<Map<String, Object>> getSortPositionFrequencies(int questionId, int departmentId) {
        return optionMapper.getSortPositionFrequencies(questionId, departmentId);
    }

    @Override
    public List<Option> getOptionsByQuestionId(int questionId) {
        return optionMapper.getOptionsByQuestionId(questionId);
    }

    @Override
    public List<Option> getAllOptions(int userId) {
        return optionMapper.getAllOptions(userId);
    }

    @Override
    public List<Option> getOptionsByPage(int currentPage, int pageSize, int questionId,int userId) {
        int offset = (currentPage-1)*pageSize;
        return optionMapper.getOptionsByPage(offset,pageSize,questionId,userId);
    }

    @Override
    public int getOptionCount(int questionId,int userId) {
        return optionMapper.getOptionCount(questionId,userId);
    }
}
