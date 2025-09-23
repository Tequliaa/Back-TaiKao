package TaiExam.service.Impl;

import TaiExam.annotation.CacheEvict;
import TaiExam.mapper.UserExamMapper;
import TaiExam.model.entity.DepartmentExam;
import TaiExam.model.entity.User;
import TaiExam.model.entity.UserExam;
import TaiExam.service.UserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class UserExamServiceImpl implements UserExamService {
    private final UserExamMapper userExamMapper;

    @Autowired
    public UserExamServiceImpl(UserExamMapper userExamMapper) {
        this.userExamMapper = userExamMapper;
    }
    @Override
    public List<UserExam> getUserInfoByExamId(int examId, int departmentId, int currentPage, int pageSize) {
        int offset = (currentPage-1)*pageSize;
        return userExamMapper.getUserInfoByExamId(examId,departmentId,offset,pageSize);
    }

    @Override
    public int getUserInfoCount(int examId, int departmentId){
        return userExamMapper.getUserInfoCount(examId,departmentId);
    }

    @Override
    public List<UserExam> getUserDepartmentInfoByExamId(int examId) {
        return userExamMapper.getUserDepartmentInfoByExamId(examId);
    }

    @Override
    public UserExam getUserExamByUserIdAndExamId(int userId, int examId) {
        return userExamMapper.getUserExamByUserIdAndExamId(userId,examId);
    }

    @Override
    public boolean assignExamsToUser(int userId,List<DepartmentExam> departmentExams) {
        return userExamMapper.assignExamsToUser(userId,departmentExams);
    }

    @Override
    public boolean assignExamsToUsers(List<User> users, List<DepartmentExam> departmentExams) {
        return userExamMapper.assignExamsToUsers(users,departmentExams);
    }

    @Override
    public boolean assignExamToDepartment(List<User> users, UserExam userExam){
        return userExamMapper.assignExamToDepartment(users, userExam);
    }

    @Override
    @CacheEvict(prefix = "response:details:",
            keyParams = {"examId","userId"},
            separator = ":")
    public boolean updateExamStatus(int id, String status, Timestamp completedAt) {
        System.out.println("到服务层实现了");
        return userExamMapper.updateExamStatus(id,status,completedAt);
    }

    @Override
    @CacheEvict(prefix = "response:details:",
            keyParams = {"examId","userId"},
            separator = ":")
    public boolean updateExamStatusByExamAndUser(int examId, int userId, String status, Timestamp completedAt) {
        return userExamMapper.updateExamStatusByExamAndUser(examId,userId,status,completedAt);
    }

    @Override
    public List<UserExam> getExamsByUserId(int userId, String keyword, int currentPage, int pageSize) {
        int offset = (currentPage-1)*pageSize;
        return userExamMapper.getExamsByUserId(userId,keyword,offset,pageSize);
    }

    @Override
    public int getExamCountByUserId(int userId, String keyword) {
        return userExamMapper.getExamCountByUserId(userId,keyword);
    }
}
