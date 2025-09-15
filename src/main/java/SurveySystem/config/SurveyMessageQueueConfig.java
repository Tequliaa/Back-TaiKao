package SurveySystem.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SurveyMessageQueueConfig {

    // 问卷提交队列
    public static final String SURVEY_SUBMIT_QUEUE = "survey.submit.queue";
    // 问卷通知队列
    public static final String SURVEY_NOTIFY_QUEUE = "survey.notify.queue";

    // 交换机
    public static final String SURVEY_EXCHANGE = "survey.exchange";

    // 路由键
    public static final String SUBMIT_ROUTING_KEY = "survey.submit";
    public static final String NOTIFY_ROUTING_KEY = "survey.notify";

    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange surveyExchange() {
        // durable: 是否持久化, autoDelete: 当没有队列绑定时是否自动删除
        return new DirectExchange(SURVEY_EXCHANGE, true, false);
    }

    /**
     * 声明提交队列
     */
    @Bean
    public Queue surveySubmitQueue() {
        // durable: 是否持久化, exclusive: 是否排他, autoDelete: 当没有消费者时是否自动删除
        return QueueBuilder.durable(SURVEY_SUBMIT_QUEUE)
                .build();
    }

    /**
     * 声明通知队列
     */
    @Bean
    public Queue surveyNotifyQueue() {
        return QueueBuilder.durable(SURVEY_NOTIFY_QUEUE)
                .build();
    }

    /**
     * 绑定提交队列到交换机
     */
    @Bean
    public Binding submitBinding(Queue surveySubmitQueue, DirectExchange surveyExchange) {
        return BindingBuilder.bind(surveySubmitQueue)
                .to(surveyExchange)
                .with(SUBMIT_ROUTING_KEY);
    }

    /**
     * 绑定通知队列到交换机
     */
    @Bean
    public Binding notifyBinding(Queue surveyNotifyQueue, DirectExchange surveyExchange) {
        return BindingBuilder.bind(surveyNotifyQueue)
                .to(surveyExchange)
                .with(NOTIFY_ROUTING_KEY);
    }
}
