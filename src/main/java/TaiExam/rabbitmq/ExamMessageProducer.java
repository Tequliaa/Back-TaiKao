package TaiExam.rabbitmq;

import TaiExam.config.ExamMessageQueueConfig;
import TaiExam.model.entity.ExamSubmitMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExamMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送问卷提交消息到队列
     */
    public void sendExamSubmitMessage(ExamSubmitMessage message) {
        rabbitTemplate.convertAndSend(
                ExamMessageQueueConfig.EXAM_EXCHANGE,  // 交换机
                ExamMessageQueueConfig.SUBMIT_ROUTING_KEY,  // 路由键
                message
        );
    }

    /**
     * 发送通知消息
     */
    public void sendExamNotifyMessage(Map<String, Object> message) {
        rabbitTemplate.convertAndSend(
                ExamMessageQueueConfig.EXAM_EXCHANGE,  // 交换机
                ExamMessageQueueConfig.NOTIFY_ROUTING_KEY,  // 路由键
                message
        );
    }
}
