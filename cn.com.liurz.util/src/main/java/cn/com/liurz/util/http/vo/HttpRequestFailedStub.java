package cn.com.liurz.util.http.vo;


public interface HttpRequestFailedStub {
    void failed(Exception var1);

    void cancelled();

    void completFailed(Exception var1);
}
