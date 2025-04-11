package SurveySystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SurveyPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyPlatformApplication.class, args);
    }
} 