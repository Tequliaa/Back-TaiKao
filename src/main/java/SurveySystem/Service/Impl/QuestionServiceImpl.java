package SurveySystem.Service.Impl;

import SurveySystem.Mapper.QuestionMapper;
import SurveySystem.Model.Question;
import SurveySystem.Service.QuestionService;
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
    public List<Question> getAllQuestions(int surveyId) {
        return questionMapper.getAllQuestions(surveyId);
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
    public List<Question> getQuestionsByCategoryId(int categoryId) {
        return questionMapper.getQuestionsByCategoryId(categoryId);
    }

    @Override
    public int addQuestionAndReturnId(Question question) {
        return questionMapper.addQuestionAndReturnId(question);
    }

    @Override
    public boolean updateQuestion(Question question) {
        return questionMapper.updateQuestion(question);
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        return questionMapper.deleteQuestion(questionId);
    }

    @Override
    public List<Question> getQuestionsByPage(int currentPage, int pageSize, int surveyId, int categoryId, String keyword) {
        int offset = (currentPage - 1) * pageSize;
        return questionMapper.getQuestionsByPage(offset,pageSize,surveyId,categoryId,keyword);
    }

    @Override
    public int getQuestionCount(int surveyId, int categoryId, String keyword) {
        return questionMapper.getQuestionCount(surveyId,categoryId,keyword);
    }
}
