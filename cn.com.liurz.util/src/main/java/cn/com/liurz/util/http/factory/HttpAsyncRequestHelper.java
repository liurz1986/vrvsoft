package cn.com.liurz.util.http.factory;


import cn.com.liurz.util.http.common.RequestUtil;
import cn.com.liurz.util.http.common.ResponseUtil;
import cn.com.liurz.util.http.vo.Callback;
import cn.com.liurz.util.http.vo.Request;
import cn.com.liurz.util.http.vo.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.springframework.stereotype.Component;

@Component
public class HttpAsyncRequestHelper {

    public HttpAsyncRequestHelper() {

    }

    public void execute(CloseableHttpAsyncClient httpAsyncClient, Request request, final Callback callback) {
        RequestUtil requsetUtil = new RequestUtil();
        HttpUriRequest httpRequest = requsetUtil.parseRequest(request);
        httpAsyncClient.execute(httpRequest, new FutureCallback<HttpResponse>() {
            public void completed(HttpResponse response) {
                ResponseUtil resUtil = new ResponseUtil();
                Response parseResponse = resUtil.parseResponse(response);
                callback.completed(parseResponse);
            }

            public void failed(Exception ex) {
                callback.failed(ex);
            }

            public void cancelled() {
                callback.cancelled();
            }
        });
    }

}
