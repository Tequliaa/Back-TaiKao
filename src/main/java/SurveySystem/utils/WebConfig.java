package SurveySystem.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Environment env;

    public WebConfig(Environment env) {
        this.env = env;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name").toLowerCase();
        String uploadPath;

        if (os.contains("win")) {
            // Windows 开发环境路径
            uploadPath = "file:F:/uploads/";
        } else {
            // Linux 生产环境路径
            uploadPath = "file:/var/www/uploads/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);

        // 保留默认静态资源映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
