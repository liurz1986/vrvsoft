package cn.com.liurz.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * es配置
 * 构建RestClientBuilder、RestClient、RestHighLevelClient对象
 * 2022-11-1
 *
 */
@Configuration
public class ElasticsearchRestClient {
    @Value("${spring.data.elasticsearch.cluster-client-nodes}")
    private String clusterClientNodes;
    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;
    @Value("${spring.data.elasticsearch.http.username}")
    private String userName;
    @Value("${spring.data.elasticsearch.http.password}")
    private String password;
    @Value("${spring.data.elasticsearch.http.maxConnectNum}")
    private Integer maxConnectNum;
    @Value("${spring.data.elasticsearch.http.maxConnectPerRoute}")
    private Integer maxConnectPerRoute;

    public ElasticsearchRestClient() {
    }

    @Bean
    public HttpHost[] httpHost() {
        String[] split = this.clusterClientNodes.split(",");
        HttpHost[] httpHostArray = new HttpHost[split.length];

        for(int i = 0; i < split.length; ++i) {
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }

        return httpHostArray;
    }

    /**
     * 构建ESClientSpringFactory，同时执行ESClientSpringFactory中的init方法
     *  构建RestClientBuilder、RestClient、RestHighLevelClient
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    public ESClientSpringFactory getFactory() {
        return ESClientSpringFactory.build(this.httpHost(), this.maxConnectNum, this.maxConnectPerRoute, this.userName, this.password);
    }

    @Bean(name = {"restClient"})
    public RestClient getRestClient() {

        return this.getFactory().getClient();
    }

    @Bean(name = {"restHighLevelClient"})
    public RestHighLevelClient getRHLClient() {

        return this.getFactory().getRhlClient();
    }
}
