package cn.com.liurz.util.http.common;


import cn.com.liurz.util.http.vo.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ResponseUtil {
    Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    public ResponseUtil() {
    }

    public Response parseResponse(HttpResponse httpResponse) {
        Response res = new Response();
        if (httpResponse == null) {
            res.setCode(-1);
            return res;
        } else {
            res.setCode(httpResponse.getStatusLine().getStatusCode());
            HttpEntity httpEntity = httpResponse.getEntity();

            try {
                String string = EntityUtils.toString(httpEntity, "utf-8");
                res.setContent(string);
            } catch (IOException | ParseException var5) {
                this.logger.error("parseResponse异常", var5);
            }

            return res;
        }
    }

    public Response parseResponse(Exception ex) {
        Response res = new Response();
        res.setCode(-1);
        res.setEx(ex);
        return res;
    }
}
