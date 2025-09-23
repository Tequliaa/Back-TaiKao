package TaiExam.service;

import TaiExam.model.entity.DepartmentExam;
import TaiExam.model.entity.User;
import TaiExam.model.entity.UserExam;

import java.sql.Timestamp;
import java.util.List;

public interface UserExamService {
    List<UserExam> getUserInfoByExamId(int examId, int departmentId, int currentPage, int pageSize);
    int getUserInfoCount(int examId, int departmentId);
    List<UserExam> getUserDepartmentInfoByExamId(int examId);
    UserExam getUserExamByUserIdAndExamId(int userId, int examId);

    boolean assignExamsToUser(int userId,List<DepartmentExam> departmentExams);
    boolean assignExamsToUsers(List<User> users,List<DepartmentExam> departmentExams);
    boolean assignExamToDepartment(List<User> users, UserExam userExam) ;

    /**
     * 更新用户问卷状态
     *
     * @param id        用户问卷记录 ID
     * @param status    更新后的状态
     * @param completedAt 完成时间
     * @return 是否更新成功
     */
    boolean updateExamStatus(int id, String status,Timestamp completedAt);
    boolean updateExamStatusByExamAndUser(int examId, int userId, String status, Timestamp completedAt) ;

    /**
     * 获取指定用户的所有问卷记录
     *
     * @param userId 用户 ID
     * @return 用户的问卷记录列表
     */
    // 根据用户ID和关键字查询用户的所有问卷
    List<UserExam> getExamsByUserId(int userId, String keyword, int currentPage, int pageSize) ;

    // 获取总记录数，用于分页
    int getExamCountByUserId(int userId, String keyword);
}
