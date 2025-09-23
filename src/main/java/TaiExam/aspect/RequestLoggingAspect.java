package TaiExam.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * 请求日志切面：记录 ExamSystem.controller 包下所有接口的请求信息
 */
@Aspect  // 标记为切面类，用于拦截请求
@Component  // 交给 Spring 管理，确保能被扫描到
public class RequestLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

    /**
     * 切点：拦截 ExamSystem.controller 包及其子包下的所有类的所有方法
     * 语法说明：
     * - execution：切点表达式类型（匹配方法执行）
     * - *：返回值任意（如 String、Result、void 等）
     * - ExamSystem.controller..*：匹配 controller 包及子包下的所有类
     * - (..)：方法参数任意（无参、单参、多参都匹配）
     */
    @Before("execution(* TaiExam.controller..*(..))")
    public void logRequestInfo(JoinPoint joinPoint) {
        // 1. 获取当前请求的上下文（避免直接注入 HttpServletRequest 导致的依赖问题）
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            logger.warn("【请求日志】未获取到请求上下文，跳过记录（可能是非Web请求）");
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();

        // 2. 提取请求关键信息并打印
        logger.info("==================================== 接口请求日志 ====================================");
        logger.info("请求接口路径：{}", request.getRequestURI());          // 如 /getExamAndQuestionsById
        logger.info("请求方式：{}", request.getMethod());              // 如 GET（查询）、POST（新增）、PUT（修改）
        logger.info("请求参数（URL拼接）：{}", request.getQueryString()); // GET请求参数（如 examId=1001）
        logger.info("处理方法：{}.{}",                                // 具体哪个Controller的哪个方法处理
                joinPoint.getTarget().getClass().getSimpleName(),  // Controller类名（如 ExamController）
                joinPoint.getSignature().getName()                 // 方法名（如 getExamById）
        );
        logger.info("方法入参（全参数）：{}", Arrays.toString(joinPoint.getArgs())); // 所有参数（含POST的Body参数）
        logger.info("================================================================================");
    }
}