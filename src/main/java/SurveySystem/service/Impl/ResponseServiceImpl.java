package SurveySystem.service.Impl;

import SurveySystem.mapper.ResponseMapper;
import SurveySystem.entity.Response;
import SurveySystem.entity.vo.OptionAnalysisVO;
import SurveySystem.entity.vo.QuestionAnalysisVO;
import SurveySystem.service.ResponseService;
import SurveySystem.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
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
    public void updateFileValid(Response response) {
        responseMapper.updateFileValid(response);
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

    /**
     * 获取指定部门的问卷分析数据
     * @param questionAnalysisVOList
     */
    @Override
    public void getAnalysisData(List<QuestionAnalysisVO> questionAnalysisVOList) throws IOException {
        log.info("获取问卷分析数据 questionAnalysisVOList:{}", questionAnalysisVOList);
        List<Map<String, Object>> formatted = formatQuestions(questionAnalysisVOList);
        log.info("格式化后的问卷数据 formatted:{}", formatted);
        for (Map<String, Object> questionMap : formatted) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("inputs", questionMap);
            requestMap.put("user", "app-uhhfRWyn0zuw7dvlF4HMaZ1x");
            requestMap.put("response_mode", "blocking");
            log.info("单个问题数据 requestMap:{}", requestMap);
            String analysisData = "分析数据";
            analysisData = HttpClientUtil.doPost("https://api.dify.ai/v1/completion-messages", requestMap,"app-E1b7nwM0Or2worcZqNYQZTMD");
            JSONObject jsonObject = JSON.parseObject(analysisData);
            String summary = jsonObject.getString("answer");
            log.info("单个问题分析数据 summary:{}", summary);

        }
    }


    /**
     * 格式化问卷数据
     * 单选题和多选题的选项格式化为 A.描述(计数) 的形式
     * @param questions
     * @return
     */
    public static List<Map<String, Object>> formatQuestions(List<QuestionAnalysisVO> questions) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (QuestionAnalysisVO question : questions) {
            String totalOptionStr="";
            if (question.getQuestionType().equals("单选") || question.getQuestionType().equals("多选")) {
                Map<String, Object> questionMap = new HashMap<>();
                // 处理选项，格式化为 A.描述(计数) 的形式
                //List<String> formattedOptions = new ArrayList<>();
                List<OptionAnalysisVO> options = question.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    char optionPrefix = (char) ('A' + i);
                    OptionAnalysisVO option = options.get(i);
                    String optionStr = optionPrefix + "." + option.getDescription() + "(选择人数：" + option.getCheckCount() + ")";
                    //formattedOptions.add(optionStr);
                    totalOptionStr = totalOptionStr + optionStr + ";";
                }
                questionMap.put("options", totalOptionStr);

                // 题目内容（问题类型）
                questionMap.put("title", question.getQuestionName() + "（" + question.getQuestionType() + "）");
                result.add(questionMap);
            }
        }
        return result;
    }

}
