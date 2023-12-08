package cn.com.liurz.util.http.factory;

import cn.com.liurz.util.http.common.RequestUtil;
import cn.com.liurz.util.http.common.ResponseUtil;
import cn.com.liurz.util.http.vo.Request;
import cn.com.liurz.util.http.vo.Response;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpRequestHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpRequestHelper.class);

    public HttpRequestHelper() {
    }

    public Response request(CloseableHttpClient closeHttpClient, Request request) {
        RequestUtil reqUtil = new RequestUtil();
        HttpUriRequest parseRequest = reqUtil.parseRequest(request);
        ResponseUtil responseUtil = new ResponseUtil();
        Response parseResponse = null;

        try {
            CloseableHttpResponse execute = closeHttpClient.execute(parseRequest);
            parseResponse = responseUtil.parseResponse(execute);
        } catch (IOException var7) {
            logger.error("请求失败：" + request.getUri(), var7);
            parseResponse = responseUtil.parseResponse(var7);
        }

        return parseResponse;
    }
}
