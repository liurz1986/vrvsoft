package cn.com.liurz.util.http.vo;

public class Response {
    private int code;
    private String content;
    private Exception ex;

    public Response() {
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Exception getEx() {
        return this.ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }
}
