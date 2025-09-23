package TaiExam.service.Impl;

import TaiExam.annotation.CacheEvict;
import TaiExam.config.RedisBloomFilter;
import TaiExam.mapper.ExamMapper;
import TaiExam.model.entity.Exam;
import TaiExam.service.ExamService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamMapper examMapper;
    @Resource
    private RedisBloomFilter bloomFilter;

    @Override
    public List<Exam> getExamsByPage(int pageNum, int pageSize, String keyword, int userId, String role) {
        int offset = (pageNum - 1) * pageSize;
        return examMapper.getExamsByPage(offset, pageSize,keyword, userId, role);
    }

    @Override
    public List<Exam> getAllExams(int userId) {
        return examMapper.getAllExams(userId);
    }

    @Override
    public List<Integer> getAllExamIds() {
        return examMapper.getAllExamIds();
    }

    @Override
    public Exam getExamById(int id) {
        return examMapper.getExamById(id);
    }

    @Override
    public int getExamCount(int userId,String keyword, String role) {
        return examMapper.getExamCount(userId,keyword, role);
    }

    @Override
    public void createExam(Exam exam) {
        examMapper.createExam(exam);
        // 将新增的问卷id添加至布隆过滤器内
        bloomFilter.add(exam.getId());
    }

    @Override
    @CacheEvict(prefix = "exam:detail:",keyParams = {"id"})
    public void updateExam(Exam exam) {
        examMapper.updateExam(exam);
    }

    @Override
    @CacheEvict(prefix = "exam:detail:",keyParams = "id", batch = true)
    @CacheEvict(prefix = "response:details:", keyParams = "id", batch = true)
    public void deleteExam(int id) {
        examMapper.deleteExam(id);
    }
}
