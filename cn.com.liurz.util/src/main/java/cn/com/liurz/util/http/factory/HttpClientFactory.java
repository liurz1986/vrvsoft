package cn.com.liurz.util.http.factory;


import cn.com.liurz.util.http.common.HttpSslContextUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 * httpclient构造工厂，支持http、https 同步、异步
 */
@Component
public class HttpClientFactory {

    private CloseableHttpAsyncClient defaultAsynchClient = null;

    private CloseableHttpAsyncClient asynchClient = null;

    private CloseableHttpClient httpClient= null;

    private HttpSslContextUtil httpSslContextUtil = new HttpSslContextUtil();

    /**
     * 构造默认的异步http、https请求
     * @return
     * @throws IOReactorException
     */
    public CloseableHttpAsyncClient getDefaultAsynchClient() throws IOReactorException {
        if (this.defaultAsynchClient == null) {
            this.buildDefaultAsynchClient();
        }

        return this.defaultAsynchClient;
    }

    private synchronized void buildDefaultAsynchClient() throws IOReactorException {
        this.defaultAsynchClient = HttpAsyncClients.createDefault();
        this.defaultAsynchClient.start();
    }

    /**
     * 构造异步http、https请求
     * @return
     * @throws IOReactorException
     */
    public CloseableHttpAsyncClient getAsynchClient() throws IOReactorException {
        if (this.defaultAsynchClient == null) {
            this.buildDefaultAsynchClient();
        }

        return this.defaultAsynchClient;
    }

    private synchronized void buildAsynchClient() throws IOReactorException {
        SSLContext sslContext = httpSslContextUtil.buildSslContext();
        // 忽略证书验证
        SSLIOSessionStrategy sslioSessionStrategy = new SSLIOSessionStrategy(sslContext, SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create().register("http", NoopIOSessionStrategy.INSTANCE).register("https", sslioSessionStrategy).build();
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(true).build();
        DefaultConnectingIOReactor ioReactor = null;

        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException var8) {
            var8.printStackTrace();
        }

        PoolingNHttpClientConnectionManager pool = new PoolingNHttpClientConnectionManager(ioReactor, registry);
        pool.setMaxTotal(200);
        pool.setDefaultMaxPerRoute(200);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
        this.asynchClient = HttpAsyncClients.custom().setConnectionManager(pool).setDefaultRequestConfig(requestConfig).setConnectionManagerShared(false).setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAliveDuration = super.getKeepAliveDuration(response, context);
                if (keepAliveDuration > 30000L) {
                    keepAliveDuration = 30000L;
                }

                return keepAliveDuration;
            }
        }).build();
        this.asynchClient.start();
    }


    /**
     * 构造同步http、https请求
     * @return
     * @throws IOReactorException
     */
    public CloseableHttpClient getSynchClient() throws IOReactorException {
        if (this.httpClient == null) {
            this.buildSynchClient();
        }

        return this.httpClient;
    }

    private synchronized void buildSynchClient()  {
        SSLContext sslContext = httpSslContextUtil.buildSslContext();
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager(registry);
        pool.setMaxTotal(100);
        pool.setDefaultMaxPerRoute(20);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
        this.httpClient = HttpClients.custom().setConnectionManager(pool).setDefaultRequestConfig(requestConfig).build();
    }
}
