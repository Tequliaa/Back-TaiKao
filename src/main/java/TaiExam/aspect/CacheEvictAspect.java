package TaiExam.aspect;

import TaiExam.annotation.CacheEvict;
import TaiExam.annotation.CacheEvicts;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class CacheEvictAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheEvictAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 1. 处理单个 @CacheEvict 注解（目标方法只加了这个注解时触发）
    @AfterReturning(value = "@annotation(cacheEvict)", argNames = "joinPoint,cacheEvict")
    public void handleSingleCacheEvict(JoinPoint joinPoint, CacheEvict cacheEvict) {
        if (cacheEvict == null) return;
        // 复用原逻辑，处理单个注解
        processCacheEvict(joinPoint, new CacheEvict[]{cacheEvict});
    }

    // 2. 处理多个 @CacheEvicts 注解（目标方法加了这个注解时触发）
    @AfterReturning(value = "@annotation(cacheEvicts)", argNames = "joinPoint,cacheEvicts")
    public void handleMultipleCacheEvict(JoinPoint joinPoint, CacheEvicts cacheEvicts) {
        if (cacheEvicts == null || cacheEvicts.value().length == 0) return;
        // 复用原逻辑，处理多个注解
        processCacheEvict(joinPoint, cacheEvicts.value());
    }

    // 3. 抽取公共逻辑，避免重复代码
    private void processCacheEvict(JoinPoint joinPoint, CacheEvict[] annotations) {
        Arrays.stream(annotations).forEach(annotation -> {
            String prefix = annotation.prefix();
            String[] keyParams = annotation.keyParams();
            String separator = annotation.separator();
            boolean batch = annotation.batch();

            Object[] keyValues = getParamValues(joinPoint, keyParams);
            if (keyValues == null || keyValues.length == 0) return;

            // 拼接 Key 后缀（这里加个非空判断，避免 null 导致的异常）
            String keySuffix = Arrays.stream(keyValues)
                    .filter(Objects::nonNull) // 过滤 null 值
                    .map(String::valueOf)
                    .collect(Collectors.joining(separator));

            // 构建缓存 Key 并删除
            String cacheKeyPattern = prefix + keySuffix;
            if (batch) {
                cacheKeyPattern += "*";
            }

            // 打印日志，方便调试（确认 Key 是否正确）
            //System.out.println("准备删除的缓存 Key 模式：" + cacheKeyPattern);

            Set<String> keys = redisTemplate.keys(cacheKeyPattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                //System.out.println("成功删除缓存 Key：" + keys);
            }
            //else {
            //    System.out.println("未找到匹配的缓存 Key：" + cacheKeyPattern);
            //}
        });
    }

    // 保留原有的 getParamValues 方法（但要优化异常处理）
    private Object[] getParamValues(JoinPoint joinPoint, String[] paramNames) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) {
            System.out.println("方法无参数，无法获取 keyParams");
            return null;
        }

        Object[] values = new Object[paramNames.length];
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            values[i] = null;

            // 遍历所有方法参数，查找目标值
            for (int j = 0; j < parameters.length; j++) {
                Object arg = args[j];
                if (arg == null) continue;

                // 情况1：参数名直接匹配（如方法参数是 Long examId）
                if (parameters[j].getName().equals(paramName)) {
                    values[i] = arg;
                    break;
                }
                // 情况2：从对象参数中获取属性（如 Exam 对象取 examId）
                else {
                    try {
                        String getterName = "get" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
                        Method getter = arg.getClass().getMethod(getterName);
                        Object value = getter.invoke(arg);
                        if (value != null) {
                            values[i] = value;
                            break;
                        }
                    } catch (NoSuchMethodException e) {
                        System.out.println("参数对象 " + arg.getClass().getSimpleName());
                    } catch (Exception e) {
                        System.out.println("获取参数 " + paramName + " 失败：" + e.getMessage());
                    }
                }
            }

            // 若某个参数未找到，直接返回 null（避免后续拼接异常）
            if (values[i] == null) {
                System.out.println("未找到参数 " + paramName + " 的值");
                return null;
            }
        }
        return values;
    }
}