package SurveySystem.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private Environment env;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/logout",
                        "/captcha/generate",
                        "/captcha/verify"
                );
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