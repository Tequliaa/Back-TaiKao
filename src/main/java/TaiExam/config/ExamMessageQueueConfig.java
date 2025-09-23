package TaiExam.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExamMessageQueueConfig {

    // 问卷提交队列
    public static final String EXAM_SUBMIT_QUEUE = "exam.submit.queue";
    // 问卷通知队列
    public static final String EXAM_NOTIFY_QUEUE = "exam.notify.queue";

    // 交换机
    public static final String EXAM_EXCHANGE = "exam.exchange";

    // 路由键
    public static final String SUBMIT_ROUTING_KEY = "exam.submit";
    public static final String NOTIFY_ROUTING_KEY = "exam.notify";

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange examExchange() {
        // durable: 是否持久化, autoDelete: 当没有队列绑定时是否自动删除
        return new DirectExchange(EXAM_EXCHANGE, true, false);
    }

    /**
     * 声明提交队列
     */
    @Bean
    public Queue examSubmitQueue() {
        // durable: 是否持久化, exclusive: 是否排他, autoDelete: 当没有消费者时是否自动删除
        return QueueBuilder.durable(EXAM_SUBMIT_QUEUE)
                .build();
    }

    /**
     * 声明通知队列
     */
    @Bean
    public Queue examNotifyQueue() {
        return QueueBuilder.durable(EXAM_NOTIFY_QUEUE)
                .build();
    }

    /**
     * 绑定提交队列到交换机
     */
    @Bean
    public Binding submitBinding(Queue examSubmitQueue, DirectExchange examExchange) {
        return BindingBuilder.bind(examSubmitQueue)
                .to(examExchange)
                .with(SUBMIT_ROUTING_KEY);
    }

    /**
     * 绑定通知队列到交换机
     */
    @Bean
    public Binding notifyBinding(Queue examNotifyQueue, DirectExchange examExchange) {
        return BindingBuilder.bind(examNotifyQueue)
                .to(examExchange)
                .with(NOTIFY_ROUTING_KEY);
    }
}
