package SurveySystem.service.Impl;

import SurveySystem.entity.vo.OptionAnalysisVO;
import SurveySystem.entity.vo.QuestionAnalysisVO;
import SurveySystem.service.QuestionAnalysisService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class QuestionAnalysisServiceImpl implements QuestionAnalysisService {

    // 固定常量定义
    private static final String API_URL = "https://api.dify.ai/v1/completion-messages";
    private static final String API_USER = "app-uhhfRWyn0zuw7dvlF4HMaZ1x";
    private static final String API_TOKEN = "app-E1b7nwM0Or2worcZqNYQZTMD";

    /**
     * 流式处理问卷分析，通过SseEmitter向客户端推送结果
     * @param questionAnalysisVOList 问卷问题列表
     * @param emitter SSE发射器
     */
    @Transactional
    @Override
    public void getAnalysisDataStream(List<QuestionAnalysisVO> questionAnalysisVOList, SseEmitter emitter) throws IOException {
        log.info("开始流式处理问卷分析数据，共{}个问题", questionAnalysisVOList.size());

        // 1. 格式化所有问卷数据
        List<Map<String, Object>> formattedQuestions = formatQuestionData(questionAnalysisVOList);
        log.info("成功格式化{}个有效问题数据", formattedQuestions.size());

        if (formattedQuestions.isEmpty()) {
            log.warn("没有可分析的有效问题数据");
            emitter.send("没有可分析的有效问题数据");
            emitter.complete();
            return;
        }

        // 2. 构建专业提示词
        String prompt = buildAnalysisPrompt();
        log.debug("构建的提示词: {}", prompt);

        // 3. 构建完整请求参数
        Map<String, Object> requestMap = buildRequestParams(formattedQuestions, prompt);
        requestMap.put("response_mode", "streaming"); // 设置为流式响应模式

        StringBuilder fullAnalysis = new StringBuilder();

        try {
            // 4. 流式调用AI接口
            getStreamAnalysis(requestMap, (analysisSegment) -> {
                try {
                    fullAnalysis.append(analysisSegment);
                    // 发送流式分析片段
                    log.info("接收到AI分析片段: {}", analysisSegment);
                    emitter.send(SseEmitter.event()
                            .name("analysis")
                            .data(analysisSegment)
                            .build());
                } catch (IOException e) {
                    log.error("发送SSE分析片段失败", e);
                }
            });

            // 5. 完成流式响应并记录完整结果
            emitter.complete();
            log.info("AI分析完整结果: {}", fullAnalysis.toString());
        } catch (Exception e) {
            emitter.completeWithError(e);
            throw e;
        }
    }

    /**
     * 格式化问卷数据为AI可理解的结构
     */
    private List<Map<String, Object>> formatQuestionData(List<QuestionAnalysisVO> questions) {
        List<Map<String, Object>> formattedList = new ArrayList<>();

        for (QuestionAnalysisVO question : questions) {
            // 只处理单选和多选题
            if ("单选".equals(question.getQuestionType()) || "多选".equals(question.getQuestionType())) {
                Map<String, Object> questionMap = new HashMap<>(3);

                // 计算总投票数
                int totalVotes = question.getOptions().stream()
                        .mapToInt(OptionAnalysisVO::getCheckCount)
                        .sum();

                // 处理选项字符串（包含占比计算）
                StringBuilder optionsBuilder = new StringBuilder();
                List<OptionAnalysisVO> options = question.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    char optionLetter = (char) ('A' + i);
                    OptionAnalysisVO option = options.get(i);
                    double percentage = totalVotes > 0 ?
                            (option.getCheckCount() * 100.0 / totalVotes) : 0;

                    optionsBuilder.append(optionLetter)
                            .append(".")
                            .append(option.getDescription())
                            .append("(选择人数：")
                            .append(option.getCheckCount())
                            .append("，占比：")
                            .append(String.format("%.1f%%", percentage))
                            .append("); ");
                }

                // 存储格式化后的问题数据
                questionMap.put("title", question.getQuestionName());
                questionMap.put("type", question.getQuestionType());
                questionMap.put("options", optionsBuilder.toString().trim());
                formattedList.add(questionMap);
            }
        }

        return formattedList;
    }

    /**
     * 构建专业的AI提示词
     */
    private String buildAnalysisPrompt() {
        StringBuilder promptBuilder = new StringBuilder();

        // 角色定位
        promptBuilder.append("你是专业的数据分析专家，擅长问卷数据解读和趋势分析。\n\n");

        // 任务说明
        promptBuilder.append("请分析提供的问卷问题及选项数据，每个问题包含：\n")
                .append("- title：问题描述\n")
                .append("- options：选项列表，包含每个选项的选择人数和占比\n\n");

        // 分析要求
        promptBuilder.append("请按照以下结构输出分析结果：\n")
                .append("1. 单题分析：针对每个问题，指出最受欢迎的选项、占比情况及可能原因\n")
                .append("2. 趋势总结：提炼所有问题中反映的共同趋势或用户偏好\n")
                .append("3. 行动建议：基于数据给出3条具体、可落地的改进建议\n\n")
                .append("注意：分析需简洁明了，语言风格专业客观。");

        return promptBuilder.toString();
    }

    /**
     * 构建符合API要求的请求参数
     */
    private Map<String, Object> buildRequestParams(List<Map<String, Object>> data, String prompt) {
        Map<String, Object> requestMap = new HashMap<>(3);

        // 构建inputs对象（包含API要求的所有字段）
        Map<String, Object> inputs = new HashMap<>(4);
        inputs.put("title", "问卷数据分析请求"); // API必填字段
        inputs.put("query", prompt);             // 提示词放入query字段
        inputs.put("questions", data);           // 格式化的业务数据

        // 将所有选项合并为一个字符串（段落形式）
        StringBuilder allOptionsBuilder = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> question = data.get(i);
            allOptionsBuilder.append("问题").append(i + 1).append("选项：")
                    .append(question.get("options"))
                    .append("。"); // 用句号分隔不同问题的选项
        }
        inputs.put("options", allOptionsBuilder.toString().trim()); // 字符串类型的options

        requestMap.put("inputs", inputs);
        requestMap.put("user", API_USER);            // 用户标识

        return requestMap;
    }

    /**
     * 流式调用AI接口获取分析结果
     * @param requestMap 请求参数
     * @param callback 回调函数，用于处理每个返回的分析片段
     */
    public void getStreamAnalysis(Map<String, Object> requestMap, Consumer<String> callback) throws IOException {
        log.info("开始流式调用AI分析接口");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(API_URL);
            httpPost.setHeader("Authorization", "Bearer " + API_TOKEN);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(requestMap)));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            try {
                                // 解析流式响应数据
                                String jsonStr = line.substring(6);
                                JSONObject jsonObject = JSON.parseObject(jsonStr);
                                if (jsonObject.containsKey("answer")) {
                                    String analysisSegment = jsonObject.getString("answer");
                                    if (analysisSegment != null && !analysisSegment.isEmpty()) {
                                        callback.accept(analysisSegment);
                                    }
                                }
                            } catch (Exception e) {
                                log.error("解析流式分析数据失败: {}", line, e);
                            }
                        }
                    }
                }
            }
        }
    }
}