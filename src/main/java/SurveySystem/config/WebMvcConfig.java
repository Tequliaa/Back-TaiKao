package SurveySystem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private Environment env;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 业务免鉴权路径
                        "/user/login", "/user/logout", "/user/register",
                        "/captcha/generate", "/captcha/verify",


                        // OpenAPI 3.x + Knife4j 4.x 必须排除的路径
                        "/doc.html",           // Knife4j 首页
                        "/webjars/**",         // 静态资源
                        "/v3/api-docs/**",     // OpenAPI 文档端点
                        "/v3/api-docs",        // OpenAPI 文档端点（无后缀）
                        "/swagger-resources",  // Swagger 资源
                        "/swagger-resources/**", // Swagger 资源
                        "/swagger-ui/**",      // Swagger UI
                        "/swagger-ui.html",    // Swagger UI 页面
                        "/error",              // 错误页面（重要！）
                        "/favicon.ico",         // 网站图标

                        "/ws/**" //WebSocket连接放行

                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
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

        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
}