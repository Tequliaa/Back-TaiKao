package SurveySystem.service.Impl;

import SurveySystem.annotation.CacheEvict;
import SurveySystem.mapper.QuestionMapper;
import SurveySystem.entity.Question;
import SurveySystem.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

    @Autowired
    public QuestionServiceImpl(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Override
    public List<Question> getAllQuestions(int surveyId,int userId) {
        return questionMapper.getAllQuestions(surveyId,userId);
    }

    @Override
    public Question getQuestionById(int questionId) {
        return questionMapper.getQuestionById(questionId);
    }

    @Override
    public List<Question> getQuestionsBySurveyId(int surveyId) {
        return questionMapper.getQuestionsBySurveyId(surveyId);
    }

    @Override
    @CacheEvict(prefix = "survey:detail:",keyParams = {"surveyId"})
    public int addQuestionAndReturnId(Question question) {
        return questionMapper.addQuestionAndReturnId(question);
    }

    @Override
    @CacheEvict(prefix = "survey:detail:",keyParams = {"surveyId"})
    public boolean updateQuestion(Question question) {
        return questionMapper.updateQuestion(question);
    }

    @Override
    @CacheEvict(prefix = "survey:detail:",keyParams = {"surveyId"})
    public boolean deleteQuestion(int questionId) {
        return questionMapper.deleteQuestion(questionId);
    }

    @Override
    public List<Question> getQuestionsByPage(int currentPage, int pageSize, int surveyId, int categoryId, String keyword,int userId) {
        int offset = (currentPage - 1) * pageSize;
        return questionMapper.getQuestionsByPage(offset,pageSize,surveyId,categoryId,keyword,userId);
    }

    @Override
    public int getQuestionCount(int surveyId, int categoryId, String keyword,int userId) {
        return questionMapper.getQuestionCount(surveyId,categoryId,keyword,userId);
    }
}
