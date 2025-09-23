package TaiExam.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtils {

    // 生成一个自动登录的令牌（可以更安全地实现）
    public static String generateToken(String username, String passwordHash) {
        String combined = username + ":" + passwordHash + ":" + System.currentTimeMillis();
        return hashString(combined);
    }

    // 使用 SHA-256 生成哈希
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法生成哈希值", e);
        }
    }

    // 生成随机盐
    public static String getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 加密密码
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // 校验密码
    public static boolean verifyPassword(String enteredPassword, String storedPassword, String salt) {
        String hashedEnteredPassword = hashPassword(enteredPassword, salt);
        return hashedEnteredPassword.equals(storedPassword);
    }
}
