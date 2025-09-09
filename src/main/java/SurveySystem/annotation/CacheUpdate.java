package SurveySystem.annotation;

import java.lang.annotation.*;

/**
 * 支持多参数拼接的缓存更新注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(CacheUpdates.class) // 保持可重复标注能力
public @interface CacheUpdate {

    // 缓存前缀（如"response:details:"）
    String prefix();

    // 关键：支持多个参数名（如{"surveyId", "userId"}）
    String[] keyParams();

    // 参数之间的分隔符（默认用":"，如surveyId:userId）
    String separator() default ":";

    // 是否需要批量删除（针对前缀匹配，如"response:details:1001:*"）
    boolean batch() default false;
}