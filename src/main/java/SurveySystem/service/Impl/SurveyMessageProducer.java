package SurveySystem.service.Impl;

import SurveySystem.config.SurveyMessageQueueConfig;
import SurveySystem.entity.SurveySubmitMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SurveyMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送问卷提交消息到队列
     */
    public void sendSurveySubmitMessage(SurveySubmitMessage message) {
        rabbitTemplate.convertAndSend(
                SurveyMessageQueueConfig.SURVEY_EXCHANGE,  // 交换机
                SurveyMessageQueueConfig.SUBMIT_ROUTING_KEY,  // 路由键
                message
        );
    }

    /**
     * 发送通知消息
     */
    public void sendSurveyNotifyMessage(Map<String, Object> message) {
        rabbitTemplate.convertAndSend(
                SurveyMessageQueueConfig.SURVEY_EXCHANGE,  // 交换机
                SurveyMessageQueueConfig.NOTIFY_ROUTING_KEY,  // 路由键
                message
        );
    }
}
