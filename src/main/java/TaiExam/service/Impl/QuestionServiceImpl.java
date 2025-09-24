package TaiExam.service.Impl;

import TaiExam.annotation.CacheEvict;
import TaiExam.model.entity.Option;
import TaiExam.mapper.QuestionMapper;
import TaiExam.model.entity.Question;
import TaiExam.service.OptionService;
import TaiExam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

    @Autowired
    private OptionService optionService;

    @Autowired
    public QuestionServiceImpl(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Override
    public List<Question> getAllQuestions(int examId,int userId) {
        return questionMapper.getAllQuestions(examId,userId);
    }

    @Override
    public Question getQuestionById(int questionId) {
        return questionMapper.getQuestionById(questionId);
    }

    @Override
    public List<Question> getQuestionsByExamId(int examId) {
        return questionMapper.getQuestionsByExamId(examId);
    }

    @Override
    @CacheEvict(prefix = "exam:detail:",keyParams = {"examId"})
    public int addQuestionAndReturnId(Question question) {
        return questionMapper.addQuestionAndReturnId(question);
    }

    @Override
    @CacheEvict(prefix = "exam:detail:",keyParams = {"examId"})
    public boolean updateQuestion(Question question) {
        return questionMapper.updateQuestion(question);
    }

    @Override
    @CacheEvict(prefix = "exam:detail:",keyParams = {"examId"})
    public boolean deleteQuestion(int questionId) {
        List<Option> options = optionService.getOptionsByQuestionId(questionId);
        if(options!=null&&options.size()>0){
            for(Option option:options){
                optionService.deleteOption(option.getId());
            }
        }
        return questionMapper.deleteQuestion(questionId);
    }

    @Override
    public List<Question> getQuestionsByPage(int currentPage, int pageSize, int examId, int categoryId, String keyword,int userId) {
        int offset = (currentPage - 1) * pageSize;
        return questionMapper.getQuestionsByPage(offset,pageSize,examId,categoryId,keyword,userId);
    }

    @Override
    public int getQuestionCount(int examId, int categoryId, String keyword,int userId) {
        return questionMapper.getQuestionCount(examId,categoryId,keyword,userId);
    }
}
