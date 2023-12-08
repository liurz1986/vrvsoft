package cn.com.liurz.tkmybatis.util;

public  enum ResultCode {
    FAIL(-1, "未知的错误"),
    SUCCESS(0, "成功"),
    Unauthorized(403, "未授权的请求");

    private Integer code;
    private String msg;

    private ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
