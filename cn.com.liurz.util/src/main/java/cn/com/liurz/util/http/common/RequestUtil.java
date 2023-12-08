package cn.com.liurz.util.http.common;

import cn.com.liurz.util.http.constant.RequestBodyTypeEnum;
import cn.com.liurz.util.http.constant.RequestTypeEnum;
import cn.com.liurz.util.http.vo.Request;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 构造HttpPost、HttpGet对象
 */
public class RequestUtil {
    Logger logger = LoggerFactory.getLogger(RequestUtil.class);
    Gson gson = new Gson();

    public RequestUtil() {
    }

    public HttpUriRequest parseRequest(Request request) {
        if (RequestTypeEnum.post.equals(request.getType())) {
            HttpPost httppost = this.buildPost(request);
            return httppost;
        } else {
            HttpGet httpget = this.buildGet(request);
            return httpget;
        }
    }

    private HttpGet buildGet(Request request) {
        Map<String, String> params = request.getParams();
        String string = "";
        if (params != null && params.size() > 0) {
            HttpEntity entity = this.buildHttpEntity(params, request.getEncoding());
            if (entity != null) {
                try {
                    string = EntityUtils.toString(entity, request.getEncoding());
                } catch (IOException | ParseException var6) {
                    this.logger.error("buildGet异常", var6);
                }
            }
        }

        String uri = request.getUri();
        if (!string.isEmpty()) {
            uri = uri + "?" + string;
        }

        HttpGet httpget = new HttpGet(uri);
        this.addHeaders(httpget, request);
        return httpget;
    }

    private HttpPost buildPost(Request request) {
        HttpPost httppost = new HttpPost(request.getUri());
        this.addHeaders(httppost, request);
        Map<String, String> params = request.getParams();
        if (params != null && params.size() > 0) {
            if (RequestBodyTypeEnum.form.equals(request.getBodyTyep())) {
                HttpEntity buildHttpEntity = this.buildHttpEntity(params, request.getEncoding());
                httppost.setEntity(buildHttpEntity);
            } else {
                StringEntity entity = new StringEntity(this.gson.toJson(params), ContentType.APPLICATION_JSON);
                httppost.setEntity(entity);
            }
        }

        return httppost;
    }

    private void addHeaders(HttpUriRequest http, Request request) {
        Map<String, String> headers = request.getHeader();
        if (headers != null && headers.size() > 0) {
            Iterator var4 = headers.keySet().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                http.addHeader(key, (String)headers.get(key));
            }
        }

    }

    private HttpEntity buildHttpEntity(Map<String, String> params, String encoding) {
        List<NameValuePair> pa = this.buildNameValuePair(params);
        UrlEncodedFormEntity entity = null;

        try {
            entity = new UrlEncodedFormEntity(pa, encoding);
        } catch (UnsupportedEncodingException var6) {
            this.logger.error("buildHttpEntity异常", var6);
        }

        return entity;
    }

    private List<NameValuePair> buildNameValuePair(Map<String, String> params) {
        List<NameValuePair> pa = new ArrayList();
        Iterator var3 = params.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            NameValuePair basicNameValuePair = new BasicNameValuePair(key, (String)params.get(key));
            pa.add(basicNameValuePair);
        }

        return pa;
    }
}
