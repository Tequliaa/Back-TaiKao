package SurveySystem.aspect;

import SurveySystem.annotation.CacheUpdate;
import SurveySystem.annotation.CacheUpdates;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class CacheUpdateAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheUpdateAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @AfterReturning(value = "@annotation(cacheUpdate) || @annotation(cacheUpdates)", argNames = "joinPoint,cacheUpdate,cacheUpdates")
    public void afterReturning(
            JoinPoint joinPoint,
            CacheUpdate cacheUpdate,
            CacheUpdates cacheUpdates
    ) {
        // 收集所有@CacheUpdate注解
        CacheUpdate[] annotations = resolveAnnotations(cacheUpdate, cacheUpdates);
        if (annotations == null) return;

        // 遍历处理每个注解
        Arrays.stream(annotations).forEach(annotation -> {
            // 1. 获取注解参数
            String prefix = annotation.prefix();
            String[] keyParams = annotation.keyParams(); // 多参数名（如{"surveyId", "userId"}）
            String separator = annotation.separator();   // 分隔符（如":"）
            boolean batch = annotation.batch();

            // 2. 从方法参数中获取对应的值（如surveyId=1001，userId=200）
            Object[] keyValues = getParamValues(joinPoint, keyParams);
            if (keyValues == null || keyValues.length == 0) return;

            // 3. 拼接参数值（如"1001:200"）
            String keySuffix = Arrays.stream(keyValues)
                    .map(String::valueOf)
                    .collect(Collectors.joining(separator));

            // 4. 构建完整缓存Key并删除
            String cacheKeyPattern = prefix + keySuffix;
            if (batch) {
                cacheKeyPattern += "*"; // 批量匹配（如"response:details:1001:200*"）
            }
            Set<String> keys = redisTemplate.keys(cacheKeyPattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        });
    }

    // 解析单个或多个注解
    private CacheUpdate[] resolveAnnotations(CacheUpdate single, CacheUpdates multiple) {
        if (multiple != null) {
            return multiple.value();
        } else if (single != null) {
            return new CacheUpdate[]{single};
        }
        return null;
    }

    // 获取多个参数值（按keyParams顺序）
    private Object[] getParamValues(JoinPoint joinPoint, String[] paramNames) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        Object[] values = new Object[paramNames.length];
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            // 查找参数名对应的参数值
            for (int j = 0; j < parameters.length; j++) {
                if (parameters[j].getName().equals(paramName)) {
                    values[i] = args[j];
                    break;
                }
            }
        }
        return values;
    }
}
