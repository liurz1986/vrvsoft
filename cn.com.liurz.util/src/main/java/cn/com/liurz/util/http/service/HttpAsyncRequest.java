package cn.com.liurz.util.http.service;

import cn.com.liurz.util.http.vo.HttpAsyncCallback;
import cn.com.liurz.util.http.vo.HttpRequestFailedStub;
import cn.com.liurz.util.http.vo.HttpRequestInf;
import cn.com.liurz.util.http.factory.HttpAsyncRequestHelper;
import cn.com.liurz.util.http.factory.HttpClientFactory;
import cn.com.liurz.util.http.vo.Callback;
import cn.com.liurz.util.http.vo.Request;
import cn.com.liurz.util.http.vo.Response;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 异步http请求，支持http、https
 */
@Component
public class HttpAsyncRequest {
    private Logger logger = LoggerFactory.getLogger(HttpAsyncRequest.class);
    @Autowired
    private HttpAsyncRequestHelper httpRequestHelper;
    @Autowired
    private HttpClientFactory httpClientFactory;

    private CloseableHttpAsyncClient client;
    private volatile boolean inited = false;

    public HttpAsyncRequest() {
    }

    private synchronized boolean init() {
        if (!this.inited) {
            try {
                this.client = this.httpClientFactory.getAsynchClient();
                this.inited = true;
            } catch (IOReactorException var2) {
                this.logger.error("HttpAsyncRequest init异常", var2);
            }
        }

        return this.inited;
    }

    public <T> void execute(HttpRequestInf<T> httpRequest, HttpAsyncCallback<T> callback) {
        final Request request = httpRequest.request();
        this.execute(httpRequest, callback, new HttpRequestFailedStub() {
            public void failed(Exception ex) {
                HttpAsyncRequest.this.logger.error("请求失败：" + request.getUri(), ex);
            }

            public void cancelled() {
                HttpAsyncRequest.this.logger.warn("请求被取消：" + request.getUri());
            }

            public void completFailed(Exception ex) {
                HttpAsyncRequest.this.logger.error("请求返回值验证失败：" + request.getUri(), ex);
            }
        });
    }

    public <T> void execute(final HttpRequestInf<T> httpRequest, final HttpAsyncCallback<T> callback, final HttpRequestFailedStub failedStub) {
        if (this.init()) {
            final Request request = httpRequest.request();
            this.httpRequestHelper.execute(client, request, new Callback() {
                public void failed(Exception ex) {
                    failedStub.failed(ex);
                }

                public void completed(Response response) {
                    if (response.getCode() == 200) {
                        try {
                            String content = response.getContent();
                            T format = httpRequest.format(content);
                            boolean varifyResult = httpRequest.varify(format);
                            if (varifyResult) {
                                callback.dowork(format);
                            } else {
                                failedStub.completFailed(new Exception("请求返回值验证失败：" + request.getUri()));
                            }
                        } catch (Exception var5) {
                            failedStub.completFailed(new Exception(var5));
                        }
                    } else {
                        failedStub.completFailed(response.getEx());
                    }

                }

                public void cancelled() {
                    failedStub.cancelled();
                }
            });
        }
    }

}