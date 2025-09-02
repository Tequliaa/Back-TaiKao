package SurveySystem.Utils;

import SurveySystem.Model.CaptchaInfo;
import SurveySystem.Utils.CaptchaUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码服务类
 */
@Service
public class CaptchaService {

    // 使用内存存储验证码信息（生产环境建议使用Redis）
    private final Map<String, CaptchaInfo> captchaStore = new ConcurrentHashMap<>();

    /**
     * 生成验证码
     */
    public CaptchaInfo generateCaptcha(String oldToken) {
        // 如果传入了旧token，先删除旧的验证码
        if (oldToken != null && !oldToken.isEmpty()) {
            captchaStore.remove(oldToken);
        }

        // 生成新的验证码
        CaptchaUtil.CaptchaResult result = CaptchaUtil.generateCaptcha();
        String token = UUID.randomUUID().toString().replace("-", "");

        CaptchaInfo captchaInfo = new CaptchaInfo(token, result.getCode(), result.getImageBase64());
        captchaStore.put(token, captchaInfo);

        return captchaInfo;
    }

    /**
     * 验证验证码
     */
    public boolean verifyCaptcha(String token, String inputCode) {
        if (token == null || inputCode == null) {
            return false;
        }

        CaptchaInfo captchaInfo = captchaStore.get(token);
        if (captchaInfo == null) {
            return false;
        }

        // 检查是否已使用
        if (captchaInfo.isUsed()) {
            return false;
        }

        // 检查是否过期
        if (captchaInfo.isExpired()) {
            captchaStore.remove(token);
            return false;
        }

        // 验证码验证（忽略大小写）
        boolean isValid = captchaInfo.getCode().equalsIgnoreCase(inputCode);

        if (isValid) {
            // 验证成功后标记为已使用
            captchaInfo.setUsed(true);
        }

        return isValid;
    }

    /**
     * 清理过期的验证码
     */
    public void cleanExpiredCaptcha() {
        captchaStore.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}