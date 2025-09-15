package SurveySystem.rabbitmq;

import SurveySystem.config.SurveyMessageQueueConfig;
import SurveySystem.handler.SurveyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Slf4j
public class SurveyNotifyConsumer {

    // 相当于回调函数。
    @RabbitListener(queues = SurveyMessageQueueConfig.SURVEY_NOTIFY_QUEUE)
    public void handleSurveyNotify(Map<String, Object> message) {
        try {
            // 从消息体中提取 surveyId（注意类型转换，避免空指针）
            Integer surveyId = (Integer) message.get("surveyId");
            if (surveyId == null) {
                log.error("通知消息缺少 surveyId：{}", message);
                return;
            }
            //// 处理WebSocket广播
            SurveyWebSocketHandler.broadcastToSurvey(surveyId, message);
            log.info("已向问卷 {} 广播通知：{}", surveyId, message.get("message"));
        } catch (Exception e) {
            // 处理异常
             log.error("处理问卷通知消息失败", e);
        }
    }
}
