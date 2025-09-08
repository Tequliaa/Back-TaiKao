package SurveySystem.utils;


import SurveySystem.context.BaseContext;
import SurveySystem.entity.Result;
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

    @Value("${jwt.rememberExpiration}")
    private Long rememberExpiration;

    private static final String SESSION_PREFIX = "user:session:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 对外提供读取配置的简便方法（用于 Controller）
    public long getExpiration() {
        return expiration;
    }

    public long getRememberExpiration() {
        return rememberExpiration;
    }

    // 维持原有方法（使用默认 expiration）
    public String generateToken(Long userId) {
        return generateToken(userId, expiration);
    }

    // 新增：支持自定义过期时间
    public String generateToken(Long userId, long ttlSeconds) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, ttlSeconds);
    }

    // 维持原有方法（使用默认 expiration）
    public String createToken(Map<String, Object> claims) {
        return createToken(claims, expiration);
    }

    // 新增：支持自定义过期时间
    public String createToken(Map<String, Object> claims, long ttlSeconds) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttlSeconds * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

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
        log.info("checkToken token: {}", token);
        Map<String, Object> session = getSessionFromRedis(token);
        if (session == null) {
            return Result.error("token无效或已过期");
        }
        BaseContext.setCurrentId((int) session.get("id"));
        return Result.success(session);
    }

    /**
     * 从 Redis 获取会话，并根据 JWT 的 exp 续期
     */
    private Map<String, Object> getSessionFromRedis(String token) {
        try {
            String key = SESSION_PREFIX + token;
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) return null;

            // 根据 JWT 的 exp 计算剩余有效期，进行滑动续期
            long newTtl = computeRemainingTtlByJwt(token);
            if (newTtl > 0) {
                redisTemplate.expire(key, newTtl, TimeUnit.SECONDS);
            }

            return new ObjectMapper().readValue(json,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Redis读取失败", e);
        }
    }

    /**
     * 存储会话到 Redis（默认 expiration）
     */
    public void storeSessionInRedis(String token, Map<String, Object> userInfo) {
        storeSessionInRedis(token, userInfo, expiration);
    }

    /**
     * 存储会话到 Redis（自定义 TTL）
     */
    public void storeSessionInRedis(String token, Map<String, Object> userInfo, long ttlSeconds) {
        log.info("storeSessionInRedis token: {}", token);
        try {
            String key = SESSION_PREFIX + token;
            String value = new ObjectMapper().writeValueAsString(userInfo);
            redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Redis存储失败", e);
        }
    }

    public void inValidate(String token) {
        log.info("inValidate token: {}", token);
        try {
            String key = SESSION_PREFIX + token;
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RuntimeException("Redis删除失败", e);
        }
    }

        /**
         * 基于 JWT exp 计算当前应续期的剩余 TTL（秒）
         */
        private long computeRemainingTtlByJwt(String token) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token)
                        .getBody();
                Date expirationDate = claims.getExpiration();
                long remainingMs = expirationDate.getTime() - System.currentTimeMillis();
                return Math.max(0, remainingMs / 1000);
            } catch (Exception e) {
                // 解析失败则不续期
                return 0;
            }
        }
}