package cn.com.liurz.util.http.test;

import cn.com.liurz.util.http.service.HttpSyncRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
public class HttpTest {
    private Logger logger = LoggerFactory.getLogger(HttpTest.class);
    @Autowired
    private HttpSyncRequest httpSyncRequest;

    @Test
    public void test111(){
        HttpParamTest httpParamTest = new HttpParamTest();
        String result = httpSyncRequest.getResult(httpParamTest);
        logger.info("RESULT:"+result);
    }
}
