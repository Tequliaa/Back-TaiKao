package SurveySystem.Service.Impl;

import SurveySystem.Mapper.UserSurveyMapper;
import SurveySystem.Model.User;
import SurveySystem.Model.UserSurvey;
import SurveySystem.Service.UserSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
@Service
public class UserSurveyServiceImpl implements UserSurveyService {
    private final UserSurveyMapper userSurveyMapper;

    @Autowired
    public UserSurveyServiceImpl(UserSurveyMapper userSurveyMapper) {
        this.userSurveyMapper = userSurveyMapper;
    }
    @Override
    public List<UserSurvey> getUserInfoBySurveyId(int surveyId, int departmentId, int currentPage, int pageSize) {
        int offset = (currentPage-1)*pageSize;
        return userSurveyMapper.getUserInfoBySurveyId(surveyId,departmentId,offset,pageSize);
    }

    @Override
    public int getUserInfoCount(int surveyId, int departmentId){
        return userSurveyMapper.getUserInfoCount(surveyId,departmentId);
    }

    @Override
    public List<UserSurvey> getUserDepartmentInfoBySurveyId(int surveyId) {
        return userSurveyMapper.getUserDepartmentInfoBySurveyId(surveyId);
    }

    @Override
    public UserSurvey getUserSurveyByUserIdAndSurveyId(int userId, int surveyId) {
        return userSurveyMapper.getUserSurveyByUserIdAndSurveyId(userId,surveyId);
    }

    @Override
    public boolean assignSurveyToUser(UserSurvey userSurvey) {
        return userSurveyMapper.assignSurveyToUser(userSurvey);
    }

    @Override
    public boolean assignSurveyToDepartment(List<User> users, UserSurvey userSurvey){
        return userSurveyMapper.assignSurveyToDepartment(users,userSurvey);
    }

    @Override
    public boolean updateSurveyStatus(int id, String status, Timestamp completedAt) {
        System.out.println("到服务层实现了");
        return userSurveyMapper.updateSurveyStatus(id,status,completedAt);
    }

    @Override
    public boolean updateSurveyStatusBySurveyAndUser(int surveyId, int userId, String status, Timestamp completedAt) {
        return userSurveyMapper.updateSurveyStatusBySurveyAndUser(surveyId,userId,status,completedAt);
    }

    @Override
    public List<UserSurvey> getSurveysByUserId(int userId, String keyword, int currentPage, int pageSize) {
        int offset = (currentPage-1)*pageSize;
        return userSurveyMapper.getSurveysByUserId(userId,keyword,offset,pageSize);
    }

    @Override
    public int getSurveyCountByUserId(int userId, String keyword) {
        return userSurveyMapper.getSurveyCountByUserId(userId,keyword);
    }
}
