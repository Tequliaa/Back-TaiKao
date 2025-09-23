package TaiExam.rabbitmq;

import TaiExam.config.ExamMessageQueueConfig;
import TaiExam.handler.ExamWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Slf4j
public class ExamNotifyConsumer {

    // 相当于回调函数。
    @RabbitListener(queues = ExamMessageQueueConfig.EXAM_NOTIFY_QUEUE)
    public void handleExamNotify(Map<String, Object> message) {
        try {
            // 从消息体中提取 examId（注意类型转换，避免空指针）
            Integer examId = (Integer) message.get("examId");
            if (examId == null) {
                log.error("通知消息缺少 examId：{}", message);
                return;
            }
            //// 处理WebSocket广播
            ExamWebSocketHandler.broadcastToExam(examId, message);
            log.info("已向问卷 {} 广播通知：{}", examId, message.get("message"));
        } catch (Exception e) {
            // 处理异常
             log.error("处理问卷通知消息失败", e);
        }
    }
}
