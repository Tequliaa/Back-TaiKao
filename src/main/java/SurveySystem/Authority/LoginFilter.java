package SurveySystem.Authority;

import SurveySystem.Utils.JwtUtil;
import SurveySystem.Utils.ThreadLocalUtil;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class LoginFilter implements Filter {
    private JedisPool jedisPool;

    public LoginFilter() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10); // 最大连接数
        jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379, 2000, "root@123456");
        System.out.println("JedisPool 初始化完成");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String requestURI = request.getRequestURI(); // 获取请求路径
        System.out.println("请求路径：" + requestURI);

        if (requestURI.equals("/user/login") || requestURI.equals("/user/register")) {
            // 登录和注册接口放行
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("未提供 Token");
            return;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            String redisToken = jedis.get(token);
            if (redisToken == null) {
                response.setStatus(401);
                response.getWriter().write("Token 已失效");
                return;
            }

            // 解析 Token
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);

            // 放行
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("服务器错误");
        }
    }

    @Override
    public void destroy() {
        if (jedisPool != null) {
            jedisPool.close();
        }
        System.out.println("JedisPool 已关闭");
    }
}
