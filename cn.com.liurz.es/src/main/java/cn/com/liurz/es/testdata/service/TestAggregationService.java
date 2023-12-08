package cn.com.liurz.es.testdata.service;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.client.ml.SetUpgradeModeRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.aggregations.pipeline.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 聚合运算
 */
@Service
public class TestAggregationService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RestClient restClient;

    /**
     * AggregationBuilders.terms 相当于sql中的group by
     * AggregationBuilders.cardinality：去重，计算出字段唯一值
     * AggregationBuilders.sum:求和
     * AggregationBuilders.avg：求平均值
     * AggregationBuilders.max：求最大值
     * AggregationBuilders.min：求最小值
     * AggregationBuilders.count :统计数量
     * AggregationBuilders.stats: 最大、最小、和、平均值。一起求出来
     * AggregationBuilders.extendedStats:字段的其他属性，包括最大最小，方差等等。
     * AggregationBuilders.geoBounds:  计算出所有的地理坐标将会落在一个矩形区域。比如说朝阳区域有很多饭店，我就可以用一个矩形把这些饭店都圈起来，看看范围。
     * AggregationBuilders.percentiles:百分比统计。可以看出你网站的所有页面。加载时间的差异
     * AggregationBuilders.percentileRanks:看看15毫秒和30毫秒内大概有多少页面加载完。
     * AggregationBuilders.topHits:
     * @throws IOException
     */

    public double getAageMin(){
        // 请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(TestServiceImpl.TEST_ENTITY_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 获取age(年龄的最小值)，查出来的别名为ageMin,好比sql查出来的列名的名称
        AggregationBuilder builder = AggregationBuilders.min("ageMin").field("age");
        searchSourceBuilder.aggregation(builder);
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Min min = searchResponse.getAggregations().get("ageMin");
            return min.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double getAageMax(){
        // 请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(TestServiceImpl.TEST_ENTITY_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 获取age(年龄的最小值)，查出来的别名为ageMin,好比sql查出来的列名的名称
        AggregationBuilder builder = AggregationBuilders.max("ageMax").field("age");
        searchSourceBuilder.aggregation(builder);
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Max max = searchResponse.getAggregations().get("ageMax");
            return max.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double getAageAvg(){
        // 请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(TestServiceImpl.TEST_ENTITY_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 获取age(年龄的最小值)，查出来的别名为ageMin,好比sql查出来的列名的名称
        AggregationBuilder builder = AggregationBuilders.avg("ageAvg").field("age");
        searchSourceBuilder.aggregation(builder);
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Avg avg = searchResponse.getAggregations().get("ageAvg");
            return avg.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double getAageSum(){
        // 请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(TestServiceImpl.TEST_ENTITY_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 获取age(年龄的最小值)，查出来的数据别名为ageSum,好比sql查出来的列名的名称
        AggregationBuilder builder = AggregationBuilders.sum("ageSum").field("age");
        searchSourceBuilder.aggregation(builder);
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Sum sum = searchResponse.getAggregations().get("ageSum");
            return sum.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double getAageCount(){
        // 请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(TestServiceImpl.TEST_ENTITY_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 获取age(年龄的最小值)，查出来的数据别名为ageSum,好比sql查出来的列名的名称
        AggregationBuilder builder = AggregationBuilders.count("ageCount").field("age");
        searchSourceBuilder.aggregation(builder);
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            ValueCount valueCount = searchResponse.getAggregations().get("ageCount");
            return valueCount.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String,Object> getAageStats(){
        // 请求对象
        Map<String,Object> result = new HashMap<>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(TestServiceImpl.TEST_ENTITY_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 获取age(年龄的最小值)，查出来的数据别名为ageStats,好比sql查出来的列名的名称
        AggregationBuilder builder = AggregationBuilders.stats("ageStats").field("age");
        searchSourceBuilder.aggregation(builder);
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Stats stats = searchResponse.getAggregations().get("ageStats");
            double avg =stats.getAvg();
            double sum =stats.getSum();
            double count =stats.getCount();
            double max =stats.getMax();
            double min =stats.getMin();
            result.put("avg",avg);
            result.put("sum",sum);
            result.put("count",count);
            result.put("max",max);
            result.put("min",min);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String,Object> getDocById(String id) throws IOException {
        GetRequest getRequest = new GetRequest(TestServiceImpl.TEST_ENTITY_INDEX, id);
        new HashMap();

        try {
            GetResponse getResponse = this.restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> map = getResponse.getSourceAsMap();
            return map;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void max() throws IOException {
        // 查询的对象
        SearchRequest searchRequest = new SearchRequest();
        //构造查询器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询条件查询所有
        QueryBuilder queryBuilders = QueryBuilders.matchAllQuery();
        // 需要返回字段的集合
        String[] param= {"pid"};

        // 对需要返回的数据包括哪些,不包括哪些,重复的只返回1条
        TopHitsAggregationBuilder top1 = AggregationBuilders.topHits("top").fetchSource(param, Strings.EMPTY_ARRAY).size(1);

        // 通过pid聚合并且聚合后返回10条数据,注意这里的size(这里代表聚合查询出多少条数据,注意这里的size要比最下面分页的size要大,因为是对聚合后的数据分页,如果不写的话默认是10)
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("agg").field("pid").subAggregation(top1).size(10);

        // 聚合分页:例子第一页(0,10),第二页就是(10,20)
        termsAggregationBuilder.subAggregation(new BucketSortPipelineAggregationBuilder("bucket_field",null).from(0).size(10));

        searchSourceBuilder.query(queryBuilders);
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchSourceBuilder.trackTotalHits(true); // 表示最外层hits返回的数据

        searchRequest.source(searchSourceBuilder);

        //解析返回的数据
        SearchResponse response = getSearchResponse(searchRequest);
        Terms agg = response.getAggregations().get("agg");
        for (Terms.Bucket bucket : agg.getBuckets()) {
            TopHits top = bucket.getAggregations().get("top");
            for (SearchHit searchHit : top.getHits()) {
                System.out.println(searchHit.getSourceAsMap());
            }
        }
    }
    // 执行查询并且返回response
    private SearchResponse getSearchResponse(SearchRequest searchRequest) throws IOException {
       // 注入自己的es进行查询
        SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        return response;
    }


    public static void main(String[] args){
        // 连接es
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.120.20", 9200, "http")));
        // 请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("testEntityIndex");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 获取age(年龄的最小值)，查出来的别名为ageMin
        AggregationBuilder builder = AggregationBuilders.min("ageMin").field("age");
        searchSourceBuilder.aggregation(builder);
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = null;
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    map = hit.getSourceAsMap();
                    System.out.println("map:" + Arrays.toString(map.entrySet().toArray()));
                }
            }
            Aggregations aggregations = searchResponse.getAggregations();
            List<String> list = new ArrayList<String>();
            Terms aggTerms = aggregations.get("ageMin");
            List<? extends Terms.Bucket> buckets = aggTerms.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                list.add(bucket.getKeyAsString());
            }
            System.out.println("DISTINCT list values:" + list.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
