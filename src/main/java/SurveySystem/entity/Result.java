package SurveySystem.entity;

/**
 * 统一响应结果封装类
 * 状态码规则：
 * 2xx: 成功相关（兼容HTTP状态码）
 * 4xx: 客户端错误相关
 * 5xx: 服务器错误相关
 */
public class Result<T> {
    private Integer code;        // 状态码
    private String message;      // 提示信息
    private T data;              // 响应数据
    private Integer totalCount;  // 记录总数
    private long timestamp;      // 请求时间戳

    // 基础状态码（三位，兼容HTTP）
    public static final int SUCCESS = 200;                  // 成功
    public static final int BAD_REQUEST = 400;              // 客户端请求错误
    public static final int UNAUTHORIZED = 401;             // 未认证
    public static final int FORBIDDEN = 403;                // 权限不足
    public static final int NOT_FOUND = 404;                // 资源不存在
    public static final int INTERNAL_SERVER_ERROR = 500;    // 服务器错误

    // 构造方法
    public Result(Integer code, String message, T data, Integer totalCount) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.totalCount = totalCount;
        this.timestamp = System.currentTimeMillis();
    }

    // 成功响应
    public static <E> Result<E> success(E data, int totalCount) {
        return new Result<>(SUCCESS, "操作成功", data, totalCount);
    }

    public static <E> Result<E> success(E data) {
        return new Result<>(SUCCESS, "操作成功", data, null);
    }

    public static <E> Result<E> success(E data,String message) {
        return new Result<>(SUCCESS, message, data, null);
    }

    public static Result success() {
        return new Result<>(SUCCESS, "操作成功", null, null);
    }

    // 错误响应（支持自定义状态码）
    public static Result error(int code, String message) {
        return new Result<>(code, message, null, null);
    }

    //400
    public static Result error(String message) {
        return new Result<>(BAD_REQUEST, message, null, null);
    }

    // 常用错误快捷方法
    public static Result paramError(String message) {
        return new Result<>(BAD_REQUEST, message, null, null);
    }

    public static Result unauthorized(String message) {
        return new Result<>(UNAUTHORIZED, message, null, null);
    }

    public static Result serverError(String message) {
        return new Result<>(INTERNAL_SERVER_ERROR, message, null, null);
    }

    // getter/setter
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    public long getTimestamp() { return timestamp; }
}
