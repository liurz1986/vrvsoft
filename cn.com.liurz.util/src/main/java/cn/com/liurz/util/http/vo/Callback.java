package cn.com.liurz.util.http.vo;



public interface Callback {
    void completed(Response var1);

    void failed(Exception var1);

    void cancelled();
}

