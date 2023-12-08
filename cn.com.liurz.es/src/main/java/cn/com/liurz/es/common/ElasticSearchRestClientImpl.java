package cn.com.liurz.es.common;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cn.com.liurz.es.util.DateUtil;
import cn.com.liurz.es.util.ElasticSearchUtil;
import cn.com.liurz.es.util.ResultCodeEnum;
import cn.com.liurz.es.vo.EsDocVO;
import cn.com.liurz.es.vo.FieldType;
import cn.com.liurz.es.vo.SearchField;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions.Type;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchRestClientImpl implements ElasticSearchRestClient {
    private static final String number_of_shards = "index.number_of_shards";
    private static final String number_of_replicas = "index.number_of_replicas";
    private static final String SUCCESS = "success";
    private static Map<String, Boolean> indexExistState = new HashMap();
    private static Map<String, String> indexState = new HashMap();
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchRestClientImpl.class);
    private static Gson gson = new Gson();
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public ElasticSearchRestClientImpl() {
    }

    public SearchResponse getDocs(String[] index, QueryBuilder queryBuilder, SortBuilder sortBuilder, SearchField field, int start, int size) {
        SearchRequest request = new SearchRequest();
        request.indices(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            sourceBuilder.query(queryBuilder);
        }

        if (sortBuilder != null) {
            sourceBuilder.sort(sortBuilder);
        }

        Integer maxResultWindow;
        if (field != null) {
            maxResultWindow = this.getMaxResultWindow();
            AggregationBuilder aggregationsBuilder = this.getAggregationsBuilder(field, maxResultWindow);
            sourceBuilder.aggregation(aggregationsBuilder);
        }

        sourceBuilder.from(start);
        sourceBuilder.size(size);
        sourceBuilder.trackTotalHits(true);
        request.source(sourceBuilder);
        maxResultWindow = null;

        try {
            SearchResponse searchResponse = this.restHighLevelClient.search(request, RequestOptions.DEFAULT);
            return searchResponse;
        } catch (ElasticsearchStatusException var11) {
            throw var11;
        } catch (Exception var12) {
            logger.error("getDocs异常错误：{}", var12);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var12);
        }
    }

    private Integer getMaxResultWindow() {
        Integer max_result_window = 10000;
        return max_result_window;
    }

    private AggregationBuilder getAggregationsBuilder(SearchField field, int maxResultWindow) {
        String fieldName = field.getFieldName();
        String aggsName = "aggs" + fieldName;
        FieldType fieldType = field.getFieldType();
        String timeFormat = field.getTimeFormat();
        long timeSpan = field.getTimeSpan();
        Integer size = field.getSize();
        if (size == null || size.equals(0)) {
            size = maxResultWindow;
        }

        DateHistogramInterval timeInterval = field.getTimeInterval();
        List<SearchField> childrenField = field.getChildrenField();
        AggregationBuilder aggregation = null;
        ValuesSourceAggregationBuilder var16;
        switch(fieldType) {
            case Date:
                if (timeSpan != -1L) {
                    aggregation = this.getDateAggregationBuilder(fieldName, aggsName, timeFormat, timeSpan, childrenField);
                } else {
                    aggregation = this.getDateAggregationBuilder(fieldName, aggsName, timeFormat, timeInterval, childrenField);
                }
                break;
            case String:
            case Object:
                aggregation = ((TermsAggregationBuilder)AggregationBuilders.terms(aggsName).field(fieldName)).size(size);
                break;
            case NumberSum:
                var16 = AggregationBuilders.sum(aggsName).field(fieldName);
            case NumberAvg:
                var16 = AggregationBuilders.avg(aggsName).field(fieldName);
            case NumberMax:
                aggregation = AggregationBuilders.max(aggsName).field(fieldName);
                break;
            case NumberMin:
                aggregation = AggregationBuilders.min(aggsName).field(fieldName);
                break;
            case Numberstat:
                aggregation = AggregationBuilders.stats(aggsName).field(fieldName);
                break;
            case ObjectDistinctCount:
                aggregation = AggregationBuilders.cardinality(aggsName).field(fieldName);
        }

        if (childrenField != null && childrenField.size() > 0) {
            Iterator var13 = childrenField.iterator();

            while(var13.hasNext()) {
                SearchField searchField = (SearchField)var13.next();
                if (searchField != null) {
                    AggregationBuilder aggregationsBuilder = this.getAggregationsBuilder(searchField, maxResultWindow);
                    ((AggregationBuilder)aggregation).subAggregation(aggregationsBuilder);
                }
            }
        }

        return (AggregationBuilder)aggregation;
    }

    private AggregationBuilder getDateAggregationBuilder(String fieldName, String aggsName, String timeFormat, long timeSpan, List<SearchField> childrenField) {
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = null;
        if (timeSpan > 0L) {
            dateHistogramAggregationBuilder = (DateHistogramAggregationBuilder)((DateHistogramAggregationBuilder)AggregationBuilders.dateHistogram(aggsName).field(fieldName)).interval(timeSpan).format(timeFormat);
        } else {
            dateHistogramAggregationBuilder = (DateHistogramAggregationBuilder)((DateHistogramAggregationBuilder)AggregationBuilders.dateHistogram(aggsName).field(fieldName)).format(timeFormat);
        }

        return dateHistogramAggregationBuilder;
    }

    private AggregationBuilder getDateAggregationBuilder(String fieldName, String aggsName, String timeFormat, DateHistogramInterval timeInterval, List<SearchField> childrenField) {
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = null;
        if (timeInterval != null) {
            dateHistogramAggregationBuilder = (DateHistogramAggregationBuilder)((DateHistogramAggregationBuilder)AggregationBuilders.dateHistogram(aggsName).field(fieldName)).dateHistogramInterval(timeInterval).format(timeFormat);
        } else {
            dateHistogramAggregationBuilder = (DateHistogramAggregationBuilder)((DateHistogramAggregationBuilder)AggregationBuilders.dateHistogram(aggsName).field(fieldName)).format(timeFormat);
        }

        return dateHistogramAggregationBuilder;
    }

    public Map<String, Object> getDoc(String indexName, String id) {
        GetRequest getRequest = new GetRequest(indexName, id);
        new HashMap();

        try {
            GetResponse getResponse = this.restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> map = getResponse.getSourceAsMap();
            return map;
        } catch (ElasticsearchStatusException var6) {
            throw var6;
        } catch (Exception var7) {
            logger.error("getDoc异常错误：{}", var7);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var7);
        }
    }

    public Boolean isExistEsIndex(String index) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(new String[]{index});
        boolean exists = false;

        try {
            exists = this.restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchStatusException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var6);
        }

        return exists;
    }

    public Boolean createEsIndex(String indexName, int shardCount, int repliceCount, Class<?> entityClazz) {
        Boolean result = this.isExistEsIndex(indexName);
        if (result) {
            logger.info("索引已经存在，无法创建");
            return false;
        } else {
            CreateIndexResponse createIndexResponse = null;
            XContentBuilder builder = ElasticSearchUtil.getXContentBuilder(entityClazz);
            Builder settings = Settings.builder().put("index.number_of_shards", shardCount).put("index.number_of_replicas", repliceCount).put("max_result_window", 1000000);
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(settings);
            createIndexRequest.mapping(builder);

            try {
                createIndexResponse = this.restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            } catch (ElasticsearchStatusException var13) {
                throw var13;
            } catch (Exception var14) {
                throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var14);
            }

            Boolean acknowledged = createIndexResponse.isAcknowledged();
            logger.info("索引创建情况：" + acknowledged + "  索引名称" + indexName);
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();

            for(int i = 0; i < stack.length; ++i) {
                logger.debug("类路径：" + stack[i].getClassName() + "  方法名：" + stack[i].getMethodName() + "  调用行号：" + stack[i].getLineNumber());
            }

            return acknowledged;
        }
    }

    public Boolean createEsIndex(String indexName, int shardCount, int repliceCount, Map<String, Class<?>> fields, Object obj) {
        Boolean result = this.isExistEsIndex(indexName);
        if (result) {
            logger.info("索引已经存在，无法创建");
            return false;
        } else {
            CreateIndexResponse createIndexResponse = null;
            XContentBuilder builder = ElasticSearchUtil.getXContentBuilder(fields, obj);
            Builder settings = Settings.builder().put("index.number_of_shards", shardCount).put("index.number_of_replicas", repliceCount);
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(settings);
            createIndexRequest.mapping(builder);

            try {
                createIndexResponse = this.restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            Boolean acknowledged = createIndexResponse.isAcknowledged();
            logger.info("索引创建情况：" + acknowledged);
            return acknowledged;
        }
    }

    public Boolean isExistEsTypeOfIndex(String indexName, String type) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(new String[]{indexName, type});
        boolean exists = false;

        try {
            exists = this.restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchStatusException var6) {
            throw var6;
        } catch (Exception var7) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var7);
        }

        return exists;
    }

    public String[] getAllIndex() {
        GetIndexRequest request = new GetIndexRequest(new String[]{"*"});
        String[] indices = null;

        try {
            GetIndexResponse getIndexResponse = this.restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
            indices = getIndexResponse.getIndices();
            return indices;
        } catch (ElasticsearchStatusException var4) {
            throw var4;
        } catch (Exception var5) {
            logger.info("获得索引失败:{}", var5);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var5);
        }
    }

    private String isEsIndexOpen(String indexName) {
        String esState = null;
        GetIndexRequest getIndexRequest = new GetIndexRequest(new String[]{indexName});

        try {
            boolean exists = this.restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            if (exists) {
                esState = "OPEN";
            } else {
                esState = "CLOSE";
            }

            return esState;
        } catch (Exception var5) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var5);
        }
    }

    public Boolean checkEsIndexState(String indexName) {
        Boolean existEsIndex = this.isExistEsIndex(indexName);
        return existEsIndex;
    }

    public String checkIndexStatus(String indexName) {
        if ("CLOSE".equals(this.isEsIndexOpen(indexName))) {
            indexState.put(indexName, this.isEsIndexOpen(indexName));
        }

        return (String)indexState.get(indexName);
    }

    public String createDoc(String indexName, String id, Map<String, Object> field) {
        BulkRequest bulkRequest = new BulkRequest();
        BulkResponse bulkResponse = null;

        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
            this.getXcontentBuilder(field, xContentBuilder);
            xContentBuilder.endObject();
            IndexRequest indexRequest = (new IndexRequest(indexName)).id(id).source(xContentBuilder);
            bulkRequest.add(indexRequest);
            bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
            bulkResponse = this.restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();

            for(int i = 0; i < stack.length; ++i) {
                logger.debug("类路径：" + stack[i].getClassName() + "  方法名：" + stack[i].getMethodName() + "  调用行号：" + stack[i].getLineNumber());
            }
        } catch (Exception var10) {
            logger.error("创建doc报错", var10);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var10);
        }

        if (bulkResponse.hasFailures()) {
            logger.error("创建doc报错:{}", bulkResponse.buildFailureMessage());
            return bulkResponse.buildFailureMessage();
        } else {
            return "success";
        }
    }

    private void getXcontentBuilder(Map<String, Object> field, XContentBuilder xContentBuilder) {
        try {
            Iterator var3 = field.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, Object> entry = (Entry)var3.next();
                String key = (String)entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    String typeName = value.getClass().getTypeName();
                    byte var9 = -1;
                    switch(typeName.hashCode()) {
                        case -2056817302:
                            if (typeName.equals("java.lang.Integer")) {
                                var9 = 1;
                            }
                            break;
                        case -1325958191:
                            if (typeName.equals("double")) {
                                var9 = 14;
                            }
                            break;
                        case -1114099497:
                            if (typeName.equals("java.util.ArrayList")) {
                                var9 = 15;
                            }
                            break;
                        case -528833112:
                            if (typeName.equals("java.lang.FLoat")) {
                                var9 = 5;
                            }
                            break;
                        case -515992664:
                            if (typeName.equals("java.lang.Short")) {
                                var9 = 11;
                            }
                            break;
                        case 104431:
                            if (typeName.equals("int")) {
                                var9 = 2;
                            }
                            break;
                        case 3039496:
                            if (typeName.equals("byte")) {
                                var9 = 10;
                            }
                            break;
                        case 3327612:
                            if (typeName.equals("long")) {
                                var9 = 8;
                            }
                            break;
                        case 64711720:
                            if (typeName.equals("boolean")) {
                                var9 = 4;
                            }
                            break;
                        case 65575278:
                            if (typeName.equals("java.util.Date")) {
                                var9 = 16;
                            }
                            break;
                        case 97526364:
                            if (typeName.equals("float")) {
                                var9 = 6;
                            }
                            break;
                        case 109413500:
                            if (typeName.equals("short")) {
                                var9 = 12;
                            }
                            break;
                        case 344809556:
                            if (typeName.equals("java.lang.Boolean")) {
                                var9 = 3;
                            }
                            break;
                        case 398507100:
                            if (typeName.equals("java.lang.Byte")) {
                                var9 = 9;
                            }
                            break;
                        case 398795216:
                            if (typeName.equals("java.lang.Long")) {
                                var9 = 7;
                            }
                            break;
                        case 761287205:
                            if (typeName.equals("java.lang.Double")) {
                                var9 = 13;
                            }
                            break;
                        case 1195259493:
                            if (typeName.equals("java.lang.String")) {
                                var9 = 0;
                            }
                    }

                    switch(var9) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                            xContentBuilder.field(key, value);
                            break;
                        case 15:
                            List valueList = (List)value;
                            if (valueList.size() > 0) {
                                String fieldType = valueList.get(0).getClass().getTypeName();
                                this.getContextBuilderByList(xContentBuilder, fieldType, value, key);
                            }
                            break;
                        case 16:
                            xContentBuilder.field(key, DateUtil.format((Date)value));
                            break;
                        default:
                            Gson gson = new Gson();
                            String json = gson.toJson(value);
                            Map<String, Object> fromJson = (Map)gson.fromJson(json, (new TypeToken<Map<String, Object>>() {
                            }).getType());
                            xContentBuilder.startObject(key);
                            this.getXcontentBuilder(fromJson, xContentBuilder);
                            xContentBuilder.endObject();
                    }
                }
            }

        } catch (Exception var14) {
            logger.error("getXcontentBuilder报错", var14);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var14);
        }
    }

    private void getContextBuilderByList(XContentBuilder xContentBuilder, String filedTypeName, Object value, String key) {
        try {
            byte var6 = -1;
            switch(filedTypeName.hashCode()) {
                case -2056817302:
                    if (filedTypeName.equals("java.lang.Integer")) {
                        var6 = 1;
                    }
                    break;
                case -1325958191:
                    if (filedTypeName.equals("double")) {
                        var6 = 14;
                    }
                    break;
                case -1114099497:
                    if (filedTypeName.equals("java.util.ArrayList")) {
                        var6 = 15;
                    }
                    break;
                case -528833112:
                    if (filedTypeName.equals("java.lang.FLoat")) {
                        var6 = 5;
                    }
                    break;
                case -515992664:
                    if (filedTypeName.equals("java.lang.Short")) {
                        var6 = 11;
                    }
                    break;
                case 104431:
                    if (filedTypeName.equals("int")) {
                        var6 = 2;
                    }
                    break;
                case 3039496:
                    if (filedTypeName.equals("byte")) {
                        var6 = 10;
                    }
                    break;
                case 3327612:
                    if (filedTypeName.equals("long")) {
                        var6 = 8;
                    }
                    break;
                case 64711720:
                    if (filedTypeName.equals("boolean")) {
                        var6 = 4;
                    }
                    break;
                case 65575278:
                    if (filedTypeName.equals("java.util.Date")) {
                        var6 = 16;
                    }
                    break;
                case 97526364:
                    if (filedTypeName.equals("float")) {
                        var6 = 6;
                    }
                    break;
                case 109413500:
                    if (filedTypeName.equals("short")) {
                        var6 = 12;
                    }
                    break;
                case 344809556:
                    if (filedTypeName.equals("java.lang.Boolean")) {
                        var6 = 3;
                    }
                    break;
                case 398507100:
                    if (filedTypeName.equals("java.lang.Byte")) {
                        var6 = 9;
                    }
                    break;
                case 398795216:
                    if (filedTypeName.equals("java.lang.Long")) {
                        var6 = 7;
                    }
                    break;
                case 761287205:
                    if (filedTypeName.equals("java.lang.Double")) {
                        var6 = 13;
                    }
                    break;
                case 1195259493:
                    if (filedTypeName.equals("java.lang.String")) {
                        var6 = 0;
                    }
            }

            switch(var6) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    xContentBuilder.field(key, value);
                    break;
                case 15:
                    Field declaredField = value.getClass().getDeclaredField(key);
                    String fieldType = ElasticSearchUtil.getParamterTypeByList(declaredField);
                    this.getContextBuilderByList(xContentBuilder, fieldType, value, key);
                case 16:
                    xContentBuilder.field(key, DateUtil.format((Date)value));
                default:
                    xContentBuilder.field(key, value);
            }

        } catch (Exception var9) {
            logger.error("getContextBuilderByList报错", var9);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var9);
        }
    }

    public void refreshIndexByIndexName(String indexName) {
        FlushRequest flushRequest = new FlushRequest(new String[]{indexName});

        try {
            this.restHighLevelClient.indices().flush(flushRequest, RequestOptions.DEFAULT);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public Boolean openIndexByIndexName(String indexName) {
        OpenIndexRequest openIndexRequest = new OpenIndexRequest(new String[]{indexName});
        boolean isAck = false;

        try {
            OpenIndexResponse openIndexResponse = this.restHighLevelClient.indices().open(openIndexRequest, RequestOptions.DEFAULT);
            isAck = openIndexResponse.isAcknowledged();
        } catch (Exception var5) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var5);
        }

        return isAck;
    }

    public String bulkCreateDocs(String indexName, List<EsDocVO> list) {
        BulkRequest bulkRequest = new BulkRequest();

        try {
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                EsDocVO esDocVO = (EsDocVO)var4.next();
                String idValue = esDocVO.getIdValue();
                Map<String, Object> map = esDocVO.getMap();
                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
                this.getXcontentBuilder(map, xContentBuilder);
                xContentBuilder.endObject();
                IndexRequest indexRequest = (new IndexRequest(indexName)).id(idValue).source(xContentBuilder);
                bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
                bulkRequest.add(indexRequest);
            }

            this.restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();

            for(int i = 0; i < stack.length; ++i) {
                logger.debug("类路径：" + stack[i].getClassName() + "  方法名：" + stack[i].getMethodName() + "  调用行号：" + stack[i].getLineNumber());
            }

            return null;
        } catch (Exception var10) {
            logger.error("批量插入报错：{}", var10);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var10);
        }
    }

    public boolean delIndexByIndexName(String indexName) {
        Boolean existEsIndex = this.isExistEsIndex(indexName);
        if (existEsIndex) {
            try {
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
                this.restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            } catch (IOException var4) {
                logger.error("删除索引失败：{}", var4);
            }

            return true;
        } else {
            logger.info("{}该索引不存在，请检查！", indexName);
            return false;
        }
    }

    public Boolean addAlias(String index, String aliasName) {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        AliasActions aliasActions = new AliasActions(Type.ADD);
        aliasActions.index(index).alias(aliasName);
        request.addAliasAction(aliasActions);

        try {
            this.restHighLevelClient.indices().updateAliases(request, RequestOptions.DEFAULT);
        } catch (IOException var6) {
            logger.error("创建别名出现问题，请检查！{}", var6);
        }

        return true;
    }

    public Boolean isExistIndexAlias(String index, String aliasName) {
        GetAliasesRequest request = new GetAliasesRequest(new String[]{aliasName});
        request.indices(new String[]{index});
        boolean result = false;

        try {
            result = this.restHighLevelClient.indices().existsAlias(request, RequestOptions.DEFAULT);
        } catch (IOException var6) {
            logger.error("判断索引{}出现问题，请检查！{}", index, var6);
        }

        return result;
    }

    public boolean deleteExistIndexAlias(String index, String aliasName) {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        AliasActions aliasActions = new AliasActions(Type.REMOVE);
        aliasActions.index(index).alias(aliasName);
        request.addAliasAction(aliasActions);

        try {
            this.restHighLevelClient.indices().updateAliases(request, RequestOptions.DEFAULT);
        } catch (IOException var6) {
            logger.error("删除别名出现问题，请检查！{}", var6);
        }

        return false;
    }

    public Map<String, Set<AliasMetadata>> getAliasSets(String index, String aliasName) {
        Map<String, Set<AliasMetadata>> map = new HashMap();
        GetAliasesRequest request = new GetAliasesRequest(new String[]{aliasName});
        request.indices(new String[]{index});
        try {
            GetAliasesResponse getAliasesResponse = this.restHighLevelClient.indices().getAlias(request, RequestOptions.DEFAULT);
            map = getAliasesResponse.getAliases();
        } catch (IOException var6) {
            logger.error("创建别名出现问题，请检查！{}", var6);
        }

        return map;
    }

    private SearchResponse selectConditionByScroll(QueryBuilder queryBuilder, SortBuilder sortBuilder, SearchField field, int size, SearchRequest request) {
        SearchResponse searchResponse = null;
        Integer maxResultWindow = this.getMaxResultWindow();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            builder.query(queryBuilder);
        }

        if (sortBuilder != null) {
            builder.sort(sortBuilder);
        }

        if (field != null) {
            AggregationBuilder aggregationsBuilder = this.getAggregationsBuilder(field, maxResultWindow);
            builder.aggregation(aggregationsBuilder);
        }

        if (size > 0) {
            builder.size(size);
            request.scroll(TimeValue.timeValueMinutes(60L));
            request.source(builder);

            try {
                searchResponse = this.restHighLevelClient.search(request, RequestOptions.DEFAULT);
            } catch (IOException var10) {
                logger.error("游标分页查询失败，请检查原因！", var10);
            }

            return searchResponse;
        } else {
            throw new RuntimeException("查找size小于0，请检查！");
        }
    }

    public SearchResponse getDocsByScroll(String index, QueryBuilder queryBuilder, SortBuilder sortBuilder, SearchField field, int size) {
        SearchRequest request = new SearchRequest(new String[]{index});
        SearchResponse searchResponse = this.selectConditionByScroll(queryBuilder, sortBuilder, field, size, request);
        return searchResponse;
    }

    public SearchResponse getDocsByScroll(String[] index, QueryBuilder queryBuilder, SortBuilder sortBuilder, SearchField field, int size) {
        SearchRequest request = new SearchRequest(index);
        SearchResponse searchResponse = this.selectConditionByScroll(queryBuilder, sortBuilder, field, size, request);
        return searchResponse;
    }

    public SearchResponse searchByScrollId(String scrollId) {
        SearchResponse searchResponse = null;
        SearchScrollRequest searchScrollRequest = new SearchScrollRequest();
        searchScrollRequest.scrollId(scrollId);

        try {
            searchResponse = this.restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);
        } catch (IOException var5) {
            logger.error("根据ScrollId获得对应的查询结果报错", var5);
        }

        return searchResponse;
    }

    public boolean clearScroll(String scrollId) {
        boolean result = false;
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);

        try {
            ClearScrollResponse clearScrollResponse = this.restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            if (clearScrollResponse.isSucceeded()) {
                result = true;
            }
        } catch (IOException var5) {
            logger.error("清除Scroll游标报错", var5);
        }

        return result;
    }

    public Boolean delDocByIndexName(String indexName, String id) {
        boolean result = false;
        DeleteRequest deleteRequest = new DeleteRequest(indexName, id);

        try {
            DeleteResponse delete = this.restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            int status = delete.status().getStatus();
            if (status == 200) {
                result = true;
            }
        } catch (IOException var7) {
            logger.error("根据索引和ID删除对应的数据", var7);
        }

        return result;
    }

    public Settings getSetting(String indexName) {
        GetSettingsRequest getSettingsRequest = new GetSettingsRequest();
        getSettingsRequest.indices(new String[]{indexName});
        Settings settings = null;

        try {
            GetSettingsResponse getSettingsResponse = this.restHighLevelClient.indices().getSettings(getSettingsRequest, RequestOptions.DEFAULT);
            ImmutableOpenMap<String, Settings> map = getSettingsResponse.getIndexToSettings();

            ObjectObjectCursor cursor;
            for(Iterator var6 = map.iterator(); var6.hasNext(); settings = (Settings)cursor.value) {
                cursor = (ObjectObjectCursor)var6.next();
            }
        } catch (IOException var8) {
            logger.error("获得参数异常访问：{}", var8);
        }

        return settings;
    }

    public boolean reindexTransportDate(String sourceIndexName, String destinationIndexName) {
        ReindexRequest reindexRequest = new ReindexRequest();
        reindexRequest.setSourceIndices(new String[]{sourceIndexName});
        reindexRequest.setDestIndex(destinationIndexName);
        BulkByScrollResponse byScrollResponse = null;

        try {
            byScrollResponse = this.restHighLevelClient.reindex(reindexRequest, RequestOptions.DEFAULT);
        } catch (IOException var6) {
            logger.error("转移数据异常，异常原因：{}", var6);
        }

        logger.info("转移数据成功!成功返回信息：{}", byScrollResponse);
        return true;
    }
}

