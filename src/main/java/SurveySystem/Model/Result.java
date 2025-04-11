package SurveySystem.Model;

public class Result<T> {
    private Integer code;//业务状态码  0-成功  1-失败
    private String message;//提示信息
    private T data;//响应数据
    private Integer totalCount; // 记录总数

    // **新增构造方法**
    public Result(Integer code, String message, T data, Integer totalCount) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.totalCount = totalCount;
    }

    // **新增 success 方法，支持 totalCount**
    public static <E> Result<E> success(E data, int totalCount) {
        return new Result<>(0, "操作成功", data, totalCount);
    }

    // 其他已有方法不变
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data, null);
    }

    public static Result success() {
        return new Result(0, "操作成功", null, null);
    }

    public static Result error(String message) {
        return new Result(1, message, null, null);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Result() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}

