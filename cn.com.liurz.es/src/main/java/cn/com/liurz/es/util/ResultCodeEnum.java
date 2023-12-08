package cn.com.liurz.es.util;

public enum ResultCodeEnum implements IResultCode {
    FORM_VALIDATE_ERROR(-2, "表单验证错误"),
    UNKNOW_FAILED(-1, "未知的错误"),
    SUCCESS(0, "成功"),
    Unauthorized(403, "未授权的请求"),
    ERROR(500, "程序编译出错"),
    IPIsEmpty(-1, "IP为空");

    private Integer code;
    private String msg;

    private ResultCodeEnum(Integer code, String msg) {
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