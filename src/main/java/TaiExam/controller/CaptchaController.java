package TaiExam.controller;

import TaiExam.model.entity.CaptchaInfo;
import TaiExam.model.entity.Result;
import TaiExam.utils.CaptchaService;
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
    public Result<Map<String, Object>> generateCaptcha(@RequestBody(required = false) Map<String, String> params) {
        try {
            String oldToken = params != null ? params.get("token") : null;
            CaptchaInfo captchaInfo = captchaService.generateCaptcha(oldToken);
            Map<String,Object> data = new HashMap();
            data.put("token",captchaInfo.getToken());
            data.put("imageBase64",captchaInfo.getImageBase64());

            return Result.success(data,"验证码生成成功");
            
        } catch (Exception e) {
            Result.error("验证码生成失败："+e.getMessage());
        }
        return Result.success();
    }
    
    /**
     * 验证验证码
     * POST /captcha/verify
     */
    @PostMapping("/verify")
    public Result<Map<String, Object>> verifyCaptcha(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String token = params.get("token");
            String code = params.get("code");
            
            if (token == null || code == null) {
                return Result.error("参数不能为空");
            }
            
            boolean isValid = captchaService.verifyCaptcha(token, code);
            
            if (isValid) {
                Map<String,Object> data = new HashMap();
                data.put("valid",true);
                return Result.success(data);
            } else {
                return Result.error(501,"验证码错误或已过期");
            }
            
        } catch (Exception e) {
            Result.error(500,"验证码验证失败: " + e.getMessage());
        }

        return Result.success();
    }
}