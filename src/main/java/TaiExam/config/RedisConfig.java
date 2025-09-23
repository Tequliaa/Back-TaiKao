package TaiExam.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    /**
     * 1. 通用RedisTemplate：用于普通业务数据（对象、哈希等）
     * 用JSON序列化，保留类型信息
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 1. 创建ObjectMapper并配置类型信息保留
        ObjectMapper objectMapper = new ObjectMapper();
        // 关键：开启类型信息存储（序列化时记录对象类型，反序列化时依据类型还原）
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL // 对非final类型生效（如Question、Exam等实体类）
        );
        // 支持Java 8时间类型（LocalDateTime等）
        objectMapper.registerModule(new JavaTimeModule());
        // 允许访问所有字段（包括private）
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 2. 使用带类型信息的ObjectMapper创建序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 3. 配置Key和Value的序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);          // Key用String序列化
        template.setHashKeySerializer(stringSerializer);       // Hash的Key用String序列化
        template.setValueSerializer(jsonSerializer);           // Value用带类型的JSON序列化
        template.setHashValueSerializer(jsonSerializer);       // Hash的Value用带类型的JSON序列化

        template.afterPropertiesSet();
        return template;
    }


    /**
     * 2. 专门用于BitMap的RedisTemplate：解决布隆过滤器问题
     * 用StringRedisSerializer（原生二进制支持）
     */
    @Bean(name = "bitmapRedisTemplate")
    public RedisTemplate<String, Object> bitmapRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 关键：所有序列化器都用StringRedisSerializer（原生二进制）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);  // 这里必须用StringRedisSerializer
        template.setHashValueSerializer(stringSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
