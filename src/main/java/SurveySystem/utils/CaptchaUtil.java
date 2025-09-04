package SurveySystem.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码生成工具类
 */
public class CaptchaUtil {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 50;
    private static final int CODE_LENGTH = 4;
    
    /**
     * 生成验证码图片和验证码字符串
     */
    public static CaptchaResult generateCaptcha() {
        // 生成随机验证码
        String code = generateRandomCode();
        
        // 创建图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 24));
        
        // 绘制验证码字符
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawString(String.valueOf(code.charAt(i)), 20 + i * 20, 30);
        }
        
        // 添加干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), 
                      random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }
        
        // 添加干扰点
        for (int i = 0; i < 50; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.fillOval(random.nextInt(WIDTH), random.nextInt(HEIGHT), 2, 2);
        }
        
        g.dispose();
        
        // 转换为Base64
        String imageBase64 = imageToBase64(image);
        
        return new CaptchaResult(code, imageBase64);
    }
    
    /**
     * 生成随机验证码字符串
     */
    private static String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
    
    /**
     * 将图片转换为Base64字符串
     */
    private static String imageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("图片转换失败", e);
        }
    }
    
    /**
     * 验证码结果类
     */
    public static class CaptchaResult {
        private String code;
        private String imageBase64;
        
        public CaptchaResult(String code, String imageBase64) {
            this.code = code;
            this.imageBase64 = imageBase64;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getImageBase64() {
            return imageBase64;
        }
    }
}