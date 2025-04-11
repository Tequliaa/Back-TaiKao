package SurveySystem.Service.Impl;

import SurveySystem.Mapper.ResponseMapper;
import SurveySystem.Model.Response;
import SurveySystem.Service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ResponseServiceImpl implements ResponseService {
    private final ResponseMapper responseMapper;

    @Autowired
    public ResponseServiceImpl(ResponseMapper responseMapper) {
        this.responseMapper = responseMapper;
    }
    @Override
    public boolean checkResponseExists(int userId, int surveyId) {
        return responseMapper.checkResponseExists(userId,surveyId);
    }

    @Override
    public void saveResponses(List<Response> responses) {
        responseMapper.saveResponses(responses);
    }

    @Override
    public void resetIsValidForResponses(int userId, int surveyId) {
        responseMapper.resetIsValidForResponses(userId,surveyId);
    }

    @Override
    public void updateResponse(Response response) {
        responseMapper.updateResponse(response);
    }

    @Override
    public void updateResponseData(Response response) {
        responseMapper.updateResponseData(response);
    }

    @Override
    public int countSurveyResponses(int surveyId) {
        return responseMapper.countSurveyResponses(surveyId);
    }

    @Override
    public List<Response> getSurveyResponsesSummary(int surveyId, int currentPage, int pageSize) {
        int offset = (currentPage-1)*pageSize;
        return responseMapper.getSurveyResponsesSummary(surveyId,offset,pageSize);
    }

    @Override
    public List<Response> getUserResponsesForSurvey(int surveyId, int userId) {
        return responseMapper.getUserResponsesForSurvey(surveyId,userId);
    }

    @Override
    public void saveFilePathToDatabase(Response response) {
        responseMapper.saveFilePathToDatabase(response);
    }

    @Override
    public List<Response> selectExistingFileResponses(int userId, int surveyId) {
        return responseMapper.selectExistingFileResponses(userId,surveyId);
    }

    @Override
    public void resetIsValidForResponsesExcludingIds(int userId, int surveyId, Set<Integer> excludedIds) {
        responseMapper.resetIsValidForResponsesExcludingIds(userId,surveyId,excludedIds);
    }

    @Override
    public List<Response> getExistingFileResponses(int questionId) {
        return responseMapper.selectExistingFileResponsesByQuestionId(questionId);
    }
}
