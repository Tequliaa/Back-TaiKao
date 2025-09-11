package SurveySystem.exception;

public class BusinessException extends RuntimeException {
private final Integer code; // 错误码（如1001用户不存在）
 
public BusinessException(Integer code, String message) {
       super(message);
       this.code = code;
}
 
public Integer getCode() {
       return code;
   }
}