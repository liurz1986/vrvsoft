package cn.com.liurz.util.http.service;

import cn.com.liurz.util.http.factory.HttpClientFactory;
import cn.com.liurz.util.http.factory.HttpRequestHelper;
import cn.com.liurz.util.http.vo.HttpRequestFailedStub;
import cn.com.liurz.util.http.vo.HttpRequestInf;
import cn.com.liurz.util.http.vo.Request;
import cn.com.liurz.util.http.vo.Response;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpSyncRequest {
    private Logger logger = LoggerFactory.getLogger(HttpSyncRequest.class);
    @Autowired
    private HttpRequestHelper httpRequestHelper;
    @Autowired
    private HttpClientFactory httpClientFactory;

    private CloseableHttpClient client;

    private volatile boolean inited = false;

    public HttpSyncRequest() {
    }

    private synchronized boolean init() {
        if (!this.inited) {
            try {
                this.client = this.httpClientFactory.getSynchClient();
                this.inited = true;
            } catch (IOReactorException var2) {
                this.logger.error("HttpSyncRequest init异常", var2);
            }
        }

        return this.inited;
    }

    public <T> T getResult(HttpRequestInf<T> httpRequest) {
        final Request request = httpRequest.request();
        T result = this.getResult(httpRequest, new HttpRequestFailedStub() {
            @Override
            public void failed(Exception ex) {
                HttpSyncRequest.this.logger.error("请求结果处理过程异常，url:" + request.getUri(), ex);
            }

            @Override
            public void cancelled() {
                HttpSyncRequest.this.logger.warn("请求被取消：" + request.getUri());
            }

            @Override
            public void completFailed(Exception ex) {
                HttpSyncRequest.this.logger.error("请求返回值验证失败：" + request.getUri(), ex);
            }
        });
        return result;
    }

    public <T> T getResult(HttpRequestInf<T> httpRequest, HttpRequestFailedStub failedStub) {
        if (this.init()) {
            Request request = httpRequest.request();
            Response response = this.httpRequestHelper.request(client,request);
            if (response.getCode() == 200) {
                try {
                    String content = response.getContent();
                    T format = httpRequest.format(content);
                    boolean varifyResult = httpRequest.varify(format);
                    if (varifyResult) {
                        return format;
                    }

                    failedStub.completFailed(new Exception("请求返回值验证失败：" + request.getUri()));
                } catch (Exception var8) {
                    failedStub.completFailed(var8);
                }
            } else {
                failedStub.failed(response.getEx());
            }
        }

        return null;
    }
}

