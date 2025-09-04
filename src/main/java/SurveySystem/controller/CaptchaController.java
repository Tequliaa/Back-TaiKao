package SurveySystem.controller;

import SurveySystem.entity.CaptchaInfo;
import SurveySystem.utils.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码控制器
 */
@RestController
@RequestMapping("/captcha")
@CrossOrigin(origins = "*") // 允许跨域
public class CaptchaController {
    
    @Autowired
    private CaptchaService captchaService;
    
    /**
     * 生成验证码
     * POST /captcha/generate
     */
    @PostMapping("/generate")
    public Map<String, Object> generateCaptcha(@RequestBody(required = false) Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String oldToken = params != null ? params.get("token") : null;
            CaptchaInfo captchaInfo = captchaService.generateCaptcha(oldToken);
            
            result.put("code", 0);
            result.put("message", "验证码生成成功");
            result.put("data", new HashMap<String, Object>() {{
                put("token", captchaInfo.getToken());
                put("imageBase64", captchaInfo.getImageBase64());
            }});
            
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "验证码生成失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 验证验证码
     * POST /captcha/verify
     */
    @PostMapping("/verify")
    public Map<String, Object> verifyCaptcha(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String token = params.get("token");
            String code = params.get("code");
            
            if (token == null || code == null) {
                result.put("code", 400);
                result.put("message", "参数不能为空");
                return result;
            }
            
            boolean isValid = captchaService.verifyCaptcha(token, code);
            
            if (isValid) {
                result.put("code", 0);
                result.put("message", "验证码验证成功");
                result.put("data", new HashMap<String, Object>() {{
                    put("valid", true);
                }});
            } else {
                result.put("code", 501);
                result.put("message", "验证码错误或已过期");
            }
            
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "验证码验证失败: " + e.getMessage());
        }
        
        return result;
    }
}