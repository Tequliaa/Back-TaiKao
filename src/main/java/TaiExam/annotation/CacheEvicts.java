package TaiExam.annotation;

import java.lang.annotation.*;

/**
 * 容器注解：用于存放多个@CacheUpdate注解（支持重复标注）
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvicts {
    // 存储多个@CacheUpdate注解
    CacheEvict[] value();
}
