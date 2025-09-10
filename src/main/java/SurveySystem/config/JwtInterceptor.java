package SurveySystem.config;

import SurveySystem.entity.Result;
import SurveySystem.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("JwtInterceptor preHandle");
        // 放行OPTIONS请求
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            log.info("token: " + token);

            // 验证token（本地会话检查）
            Result<Map<String, Object>> result = jwtUtil.checkToken(token);
            if (result.getCode() != 200) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Token验证失败", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}