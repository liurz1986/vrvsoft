package cn.com.liurz.es.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 构建RestClientBuilder、RestClient、RestHighLevelClient工厂
 *
 * 2022-11-1
 */
public class ESClientSpringFactory {
    private static Logger logger = LoggerFactory.getLogger(ESClientSpringFactory.class);
    public static int CONNECT_TIMEOUT_MILLIS = 100000;
    public static int SOCKET_TIMEOUT_MILLIS = 300000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 50000;
    public static int MAX_CONN_PER_ROUTE = 10;
    public static int MAX_CONN_TOTAL = 30;
    private static HttpHost[] HTTP_HOST;
    private RestClientBuilder builder;
    private RestClient restClient;
    private RestHighLevelClient restHighLevelClient;
    private static String USERNAME;
    private static String PASSWORD;
    private static ESClientSpringFactory esClientSpringFactory = new ESClientSpringFactory();

    private ESClientSpringFactory() {
    }

    public static ESClientSpringFactory build(HttpHost[] httpHostArray, Integer maxConnectNum, Integer maxConnectPerRoute, String userName, String passWord) {
        HTTP_HOST = httpHostArray;
        MAX_CONN_TOTAL = maxConnectNum;
        MAX_CONN_PER_ROUTE = maxConnectPerRoute;
        USERNAME = userName;
        PASSWORD = passWord;
        return esClientSpringFactory;
    }


    public void init() {
        this.builder = RestClient.builder(HTTP_HOST);
        this.setConnectTimeOutConfig();
        this.setHttpConnectConfigs();
        this.restClient = this.builder.build();
        this.restHighLevelClient = new RestHighLevelClient(this.builder);
        logger.info("init factory");
    }

    public void setConnectTimeOutConfig() {
        this.builder.setRequestConfigCallback((requestConfigBuilder) -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });
    }

    public void setHttpConnectConfigs() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));
        this.builder.setHttpClientConfigCallback((httpClientBuilder) -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            httpClientBuilder.disableAuthCaching();
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
    }

    public RestClient getClient() {
        return this.restClient;
    }

    public RestHighLevelClient getRhlClient() {
        return this.restHighLevelClient;
    }

    public void close() {
        if (this.restClient != null) {
            try {
                this.restClient.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

        logger.info("close");
    }
}

