package TaiExam.model.entity;

import java.time.LocalDateTime;

/**
 * 验证码信息实体类
 */
public class CaptchaInfo {
    private String token;
    private String code;
    private String imageBase64;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
    private boolean used;

    public CaptchaInfo() {}

    public CaptchaInfo(String token, String code, String imageBase64) {
        this.token = token;
        this.code = code;
        this.imageBase64 = imageBase64;
        this.createTime = LocalDateTime.now();
        this.expireTime = LocalDateTime.now().plusMinutes(5); // 5分钟过期
        this.used = false;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}