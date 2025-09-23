package TaiExam.mapper;

import TaiExam.model.entity.Response;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface ResponseMapper {
    boolean checkResponseExists(int userId, int examId);
    void saveResponses(List<Response> responses);
    void resetIsValidForResponses(int userId, int examId);
    void updateResponse(Response response);
    void updateFileValid(Response response);
    void updateResponseData(Response response);
    int countExamResponses(int examId) ;
    List<Response> getExamResponsesSummary(int examId, int offset, int pageSize);
    List<Response> getUserResponsesForExam(int examId, int userId);
    void saveFilePathToDatabase(Response response);
    // 获取用户已有的文件响应记录
    List<Response> selectExistingFileResponses(int userId, int examId);

    // 重置指定用户的响应记录的有效性，但排除指定的记录ID
    void resetIsValidForResponsesExcludingIds(int userId, int examId, Set<Integer> excludedIds);
    List<Response> selectExistingFileResponsesByQuestionId(int questionId);
}
