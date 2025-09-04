package SurveySystem.service;

import SurveySystem.entity.DepartmentSurvey;
import SurveySystem.entity.User;
import SurveySystem.entity.UserSurvey;

import java.sql.Timestamp;
import java.util.List;

public interface UserSurveyService {
    List<UserSurvey> getUserInfoBySurveyId(int surveyId, int departmentId, int currentPage, int pageSize);
    int getUserInfoCount(int surveyId, int departmentId);
    List<UserSurvey> getUserDepartmentInfoBySurveyId(int surveyId);
    UserSurvey getUserSurveyByUserIdAndSurveyId(int userId, int surveyId);

    boolean assignSurveysToUser(int userId,List<DepartmentSurvey> departmentSurveys);
    boolean assignSurveysToUsers(List<User> users,List<DepartmentSurvey> departmentSurveys);
    boolean assignSurveyToDepartment(List<User> users, UserSurvey userSurvey) ;

    /**
     * 更新用户问卷状态
     *
     * @param id        用户问卷记录 ID
     * @param status    更新后的状态
     * @param completedAt 完成时间
     * @return 是否更新成功
     */
    boolean updateSurveyStatus(int id, String status,Timestamp completedAt);
    boolean updateSurveyStatusBySurveyAndUser(int surveyId, int userId, String status, Timestamp completedAt) ;

    /**
     * 获取指定用户的所有问卷记录
     *
     * @param userId 用户 ID
     * @return 用户的问卷记录列表
     */
    // 根据用户ID和关键字查询用户的所有问卷
    List<UserSurvey> getSurveysByUserId(int userId, String keyword, int currentPage, int pageSize) ;

    // 获取总记录数，用于分页
    int getSurveyCountByUserId(int userId, String keyword);
}
