package TaiExam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
//添加 proxyTargetClass = false，强制使用 JDK 动态代理（接口代理）
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class TaiExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaiExamApplication.class, args);
    }
} 