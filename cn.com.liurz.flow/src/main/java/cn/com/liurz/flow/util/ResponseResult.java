package cn.com.liurz.flow.util;

import lombok.Data;

@Data
public class ResponseResult {

    private int code;

    private String msg;

    private Object data;

    private ResponseResult(){}

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResponseResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    private ResponseResult(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }

    public static  ResponseResult success() {
        return new ResponseResult (ResultCode.SUCCESS);
    }

    public static  ResponseResult success(Object data) {
        return new ResponseResult (ResultCode.SUCCESS, data);
    }

    public static  ResponseResult  error(ResultCode resultCode) {
        return new ResponseResult(resultCode);
    }

    public static  ResponseResult error(String msg) {
        return new ResponseResult(ResultCode.FAIL.getCode(), msg);
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
}
