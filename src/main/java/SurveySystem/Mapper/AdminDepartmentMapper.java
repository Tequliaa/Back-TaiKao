package SurveySystem.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminDepartmentMapper {
    void addMapping(int userId,int departmentId);
    void deleteMapping(int userId,int departmentId);
    List<Integer> getDepartmentsByUserId(int userId);
    void addBatchMapping(int userId, List<Integer> departmentIds);
    void deleteBatchMapping(int userId, List<Integer> departmentIds);

}
