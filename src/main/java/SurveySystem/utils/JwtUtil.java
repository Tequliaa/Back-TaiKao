package SurveySystem.utils;

import SurveySystem.context.BaseContext;
import SurveySystem.entity.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private static final String SESSION_PREFIX = "user:session:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims);
    }

    public String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // 看token是否符合规则。
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.get("userId").toString());
    }


    public Result<Map<String, Object>> checkToken(@RequestParam String token) {
        log.info("checkToken token: " + token);
        Map<String, Object> session = getSessionFromRedis(token);
        BaseContext.setCurrentId((int) session.get("id"));
        log.info("session currentId: {}" ,session.get("id"));
        log.info("BaseContext currentId: {}" ,BaseContext.getCurrentId());

        if (session == null) {
            return Result.error("token无效或已过期");
        }
        return Result.success(session);
    }

    /**
     * 从 Redis 获取会话
     */
    private Map<String, Object> getSessionFromRedis(String token) {
        try {
            String key =  SESSION_PREFIX+token;
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) return null;

            // 更新过期时间（续期）
            redisTemplate.expire(key, expiration, TimeUnit.SECONDS);

            return new ObjectMapper().readValue(json,
                    new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Redis读取失败", e);
        }
    }

    /**
     * 存储会话到 Redis（自动过期）
     */
    public void storeSessionInRedis(String token, Map<String, Object> userInfo) {
        log.info("storeSessionInRedis token: " + token);
        try {
            String key = SESSION_PREFIX+token;
            String value = new ObjectMapper().writeValueAsString(userInfo);
            redisTemplate.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Redis存储失败", e);
        }
    }


    /**
     * 存储会话到 Redis（自动过期）
     */
    public void inValidate(String token) {
        log.info("inValidate token: " + token);
        try {
            String key = SESSION_PREFIX+token;
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RuntimeException("Redis存储失败", e);
        }
    }


}
