package cn.com.liurz.util.http.vo;



import cn.com.liurz.util.http.constant.RequestBodyTypeEnum;
import cn.com.liurz.util.http.constant.RequestTypeEnum;

import java.util.Map;

public class Request {
    private String uri = "";  // url
    private Map<String, String> params; // 请求参数
    private RequestTypeEnum type;  // 请求方式：POST 、GET
    private RequestBodyTypeEnum bodyTyep;  // 参数格式：row表示json格式
    private String encoding;
    private Map<String, String> header;  // 设置请求头信息

    public Request() {
        this.type = RequestTypeEnum.get;
        this.bodyTyep = RequestBodyTypeEnum.raw;
        this.encoding = "utf-8";
    }

    public Map<String, String> getHeader() {
        return this.header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public RequestTypeEnum getType() {
        return this.type;
    }

    public void setType(RequestTypeEnum type) {
        this.type = type;
    }

    public RequestBodyTypeEnum getBodyTyep() {
        return this.bodyTyep;
    }

    public void setBodyTyep(RequestBodyTypeEnum bodyTyep) {
        this.bodyTyep = bodyTyep;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
