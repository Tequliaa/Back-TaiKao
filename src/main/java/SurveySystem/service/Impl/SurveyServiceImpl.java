package SurveySystem.service.Impl;

import SurveySystem.annotation.CacheEvict;
import SurveySystem.config.RedisBloomFilter;
import SurveySystem.mapper.SurveyMapper;
import SurveySystem.entity.Survey;
import SurveySystem.service.OptionService;
import SurveySystem.service.QuestionService;
import SurveySystem.service.SurveyService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyMapper surveyMapper;
    @Resource
    private RedisBloomFilter bloomFilter;

    @Autowired
    public SurveyServiceImpl(SurveyMapper surveyMapper) {
        this.surveyMapper = surveyMapper;
    }

    @Override
    public List<Survey> getSurveysByPage(int pageNum, int pageSize,String keyword, int userId, String role) {
        int offset = (pageNum - 1) * pageSize;
        return surveyMapper.getSurveysByPage(offset, pageSize,keyword, userId, role);
    }

    @Override
    public List<Survey> getAllSurveys(int userId) {
        return surveyMapper.getAllSurveys(userId);
    }

    @Override
    public List<Integer> getAllSurveyIds() {
        return surveyMapper.getAllSurveyIds();
    }

    @Override
    public Survey getSurveyById(int surveyId) {
        return surveyMapper.getSurveyById(surveyId);
    }

    @Override
    public int getSurveyCount(int userId,String keyword, String role) {
        return surveyMapper.getSurveyCount(userId,keyword, role);
    }

    @Override
    public void createSurvey(Survey survey) {
        surveyMapper.createSurvey(survey);
        // 将新增的问卷id添加至布隆过滤器内
        bloomFilter.add(survey.getSurveyId());
    }

    @Override
    @CacheEvict(prefix = "survey:detail:",keyParams = {"surveyId"})
    public void updateSurvey(Survey survey) {
        surveyMapper.updateSurvey(survey);
    }

    @Override
    @CacheEvict(prefix = "survey:detail:",keyParams = "surveyId", batch = true)
    @CacheEvict(prefix = "response:details:", keyParams = "surveyId", batch = true)
    public void deleteSurvey(int surveyId) {
        surveyMapper.deleteSurvey(surveyId);
    }
}
