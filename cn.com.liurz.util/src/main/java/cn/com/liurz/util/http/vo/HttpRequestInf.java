package cn.com.liurz.util.http.vo;

public interface HttpRequestInf<T> {
    Request request(); // 请求数据

    T format(String var1);  // http请求返回的数据转换

    boolean varify(T var1); // 数据格式校验
}
