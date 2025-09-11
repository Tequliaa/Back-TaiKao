package SurveySystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("问卷调查项目管理端接口文档")
                        .version("1.0")
                        .description("问卷调查系统管理端接口文档")
                        .contact(new Contact()
                                .name("开发者")));
    }
}