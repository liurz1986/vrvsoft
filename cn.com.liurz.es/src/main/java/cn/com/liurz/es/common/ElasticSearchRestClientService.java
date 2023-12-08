package cn.com.liurz.es.common;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import cn.com.liurz.es.vo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.liurz.es.util.ElasticSearchUtil;
import cn.com.liurz.es.util.QueryCondition;
import cn.com.liurz.es.util.ResultCodeEnum;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class ElasticSearchRestClientService<T> {
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchRestClientService.class);
    private Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @Autowired
    private ElasticSearchRestClient elasticSearchRestClient;
    @Value("${es.shardCount:3}")
    private int shardCount;
    @Value("${es.repliceCount:0}")
    private int repliceCount;
    @Value("${es.size:10000}")
    private int size;
    protected Class<T> clazz;
    protected String idField;

    public abstract String getIndexName();

    public ElasticSearchRestClientService() {
        ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
        this.clazz = (Class)parameterizedType.getActualTypeArguments()[0];
        this.idField = ((PrimaryKey)this.clazz.getAnnotation(PrimaryKey.class)).value();
    }

    public T getDoc(Serializable id) {
        if (id == null) {
            return null;
        } else {
            String indexName = this.getIndexName();
            Map<String, Object> map = this.elasticSearchRestClient.getDoc(indexName, id.toString());
            String json = this.gson.toJson(map);
            T t = this.gson.fromJson(json, this.clazz);
            return t;
        }
    }

    public T getDoc(String indexName, String type, Serializable id) {
        if (id == null) {
            return null;
        } else {
            Map<String, Object> map = this.elasticSearchRestClient.getDoc(indexName, id.toString());
            String json = this.gson.toJson(map);
            T t = this.gson.fromJson(json, this.clazz);
            return t;
        }
    }

    public List<T> findAll() {
        try {
            String indexName = this.getIndexName();
            String[] indexNameArr = this.getIndexListByBaseIndexName(indexName);
            SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexNameArr, (QueryBuilder)null, (SortBuilder)null, (SearchField)null, 0, this.size);
            List<T> list = this.getResultList(searchResponse);
            return list;
        } catch (ElasticsearchStatusException var5) {
            RestStatus status = var5.status();
            if (status.getStatus() == 404) {
                List<T> list = new ArrayList();
                return list;
            } else {
                throw var5;
            }
        } catch (Exception var6) {
            logger.error("数据查询出现异常：", var6);
            throw new RuntimeException(var6);
        }
    }

    public long count() {
        try {
            String indexName = this.getIndexName();
            String[] indexNameArr = this.getIndexListByBaseIndexName(indexName);
            SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexNameArr, (QueryBuilder)null, (SortBuilder)null, (SearchField)null, 0, 1);
            TotalHits totalHits = searchResponse.getHits().getTotalHits();
            long value = totalHits.value;
            return value;
        } catch (ElasticsearchStatusException var7) {
            RestStatus status = var7.status();
            if (status.getStatus() == 404) {
                return 0L;
            } else {
                logger.error("请检查索引是否存在或状态", var7);
                return 0L;
            }
        } catch (Exception var8) {
            logger.error("请检查索引是否存在或状态", var8);
            return 0L;
        }
    }

    public List<T> findAll(List<QueryCondition> conditions) {
        try {
            String indexName = this.getIndexName();
            String[] indexNameArr = this.getIndexListByBaseIndexName(indexName);
            QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
            SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexNameArr, queryBuilder, (SortBuilder)null, (SearchField)null, 0, this.size);
            List<T> list = this.getResultList(searchResponse);
            return list;
        } catch (ElasticsearchStatusException var7) {
            RestStatus status = var7.status();
            if (status.getStatus() == 404) {
                List<T> list = new ArrayList();
                return list;
            } else {
                throw var7;
            }
        } catch (Exception var8) {
            logger.error("请检查索引是否存在或状态", var8);
            throw new RuntimeException(var8);
        }
    }

    public List<T> findAll(IndexsInfoVO indexsInfoVO, List<QueryCondition> conditions) {
        try {
            QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
            SearchResponse response = this.elasticSearchRestClient.getDocs(indexsInfoVO.getIndex(), queryBuilder, (SortBuilder)null, (SearchField)null, 0, this.size);
            List<T> list = this.getResultList(response);
            return list;
        } catch (Exception var6) {
            logger.error("查询所有数据异常:{}", var6);
            return new ArrayList();
        }
    }

    public long count(List<QueryCondition> conditions) {
        try {
            String indexName = this.getIndexName();
            String[] indexNameArr = this.getIndexListByBaseIndexName(indexName);
            if (indexNameArr != null && indexNameArr.length != 0) {
                QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
                SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexNameArr, queryBuilder, (SortBuilder)null, (SearchField)null, 0, 1);
                TotalHits totalHits = searchResponse.getHits().getTotalHits();
                long value = totalHits.value;
                return value;
            } else {
                return 0L;
            }
        } catch (ElasticsearchStatusException var9) {
            RestStatus status = var9.status();
            if (status.getStatus() != 404) {
                logger.error("查询出现异常", var9);
            }

            return 0L;
        } catch (Exception var10) {
            logger.error("查询出现异常", var10);
            return 0L;
        }
    }

    public long count(IndexsInfoVO indexsInfoVO, List<QueryCondition> conditions) {
        String[] indexs = indexsInfoVO.getIndex();
        String[] var4 = indexs;
        int var5 = indexs.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String index = var4[var6];
            Boolean index_status = this.elasticSearchRestClient.checkEsIndexState(index);
            Boolean existEsIndex = this.elasticSearchRestClient.isExistEsIndex(index);
            if (!existEsIndex || !index_status) {
                logger.info(index + "索引不存在，请检查");
                throw new RuntimeException(index + "索引状态未开启，或者不存在");
            }
        }

        QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
        SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexsInfoVO.getIndex(), queryBuilder, (SortBuilder)null, (SearchField)null, 0, 1);
        TotalHits totalHits = searchResponse.getHits().getTotalHits();
        long value = totalHits.value;
        return value;
    }

    public List<T> findAll(List<QueryCondition> conditions, String value, String sort) {
        try {
            String indexName = this.getIndexName();
            String[] indexNameArr = this.getIndexListByBaseIndexName(indexName);
            QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
            SortBuilder sortBuilder = null;
            if (StringUtils.isNoneEmpty(new CharSequence[]{value}) && StringUtils.isNoneEmpty(new CharSequence[]{sort})) {
                if ("asc".equalsIgnoreCase(sort)) {
                    sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.ASC);
                } else {
                    sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.DESC);
                }
            }

            SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexNameArr, queryBuilder, sortBuilder, (SearchField)null, 0, this.size);
            List<T> list = this.getResultList(searchResponse);
            return list;
        } catch (ElasticsearchStatusException var10) {
            RestStatus status = var10.status();
            if (status.getStatus() == 404) {
                List<T> list = new ArrayList();
                return list;
            } else {
                throw var10;
            }
        } catch (Exception var11) {
            logger.error("请检查索引是否存在或状态", var11);
            throw new RuntimeException(var11);
        }
    }

    public List<T> findAll(List<QueryCondition> conditions, String value) {
        return this.findAll(conditions, value, "asc");
    }

    public List<T> findAllBySize(List<QueryCondition> conditions, String value, String sort, Integer size) {
        try {
            String indexName = this.getIndexName();
            String[] indexNameArr = this.getIndexListByBaseIndexName(indexName);
            QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
            SortBuilder sortBuilder = null;
            if (StringUtils.isNoneEmpty(new CharSequence[]{value}) && StringUtils.isNoneEmpty(new CharSequence[]{sort})) {
                if ("asc".equalsIgnoreCase(sort)) {
                    sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.ASC);
                } else {
                    sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.DESC);
                }
            }

            SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexNameArr, queryBuilder, sortBuilder, (SearchField)null, 0, size);
            List<T> list = this.getResultList(searchResponse);
            return list;
        } catch (ElasticsearchStatusException var11) {
            RestStatus status = var11.status();
            if (status.getStatus() == 404) {
                List<T> list = new ArrayList();
                return list;
            } else {
                throw var11;
            }
        } catch (Exception var12) {
            logger.error("请检查索引是否存在或状态", var12);
            throw new RuntimeException(var12);
        }
    }

    public PageRes<T> findByPage(PageReq pageReq, List<QueryCondition> conditions) {
        PageRes pageRes_ES;
        try {
            String indexName = this.getIndexName();
            String[] indexArr = this.getIndexListByBaseIndexName(indexName);
            if (indexArr != null && indexArr.length != 0) {
                String by_ = pageReq.getBy_();
                Integer start_ = pageReq.getStart_();
                Integer count_ = pageReq.getCount_();
                String order_ = pageReq.getOrder_();
                QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
                SortBuilder sortBuilder = null;
                if (StringUtils.isNoneEmpty(new CharSequence[]{by_}) && StringUtils.isNoneEmpty(new CharSequence[]{order_})) {
                    if ("asc".equalsIgnoreCase(by_)) {
                        sortBuilder = SortBuilders.fieldSort(order_).order(SortOrder.ASC);
                    } else {
                        sortBuilder = SortBuilders.fieldSort(order_).order(SortOrder.DESC);
                    }
                }

                SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexArr, queryBuilder, sortBuilder, (SearchField)null, start_, count_);
                PageRes<T> paginationResponse = this.getPaginationResponse(searchResponse);
                return paginationResponse;
            } else {
                pageRes_ES = new PageRes();
                pageRes_ES.setTotal(0L);
                return null;
            }
        } catch (ElasticsearchStatusException var13) {
            RestStatus status = var13.status();
            if (status.getStatus() != 404) {
                logger.error("查询出现异常", var13);
                throw var13;
            } else {
                pageRes_ES = new PageRes();
                pageRes_ES.setTotal(0L);
                pageRes_ES.setList(new ArrayList());
                pageRes_ES.setCode(ResultCodeEnum.SUCCESS.getCode().toString());
                return pageRes_ES;
            }
        } catch (Exception var14) {
            PageRes<T> pageRes = new PageRes();
            pageRes.setTotal(0L);
            pageRes.setList(new ArrayList());
            pageRes.setMessage(var14.getMessage());
            pageRes.setCode(ResultCodeEnum.UNKNOW_FAILED.getCode().toString());
            return pageRes;
        }
    }

    public PageRes<T> findByPage(IndexsInfoVO indexsInfoVO, PageReq pageReq, List<QueryCondition> conditions) {
        String by_ = pageReq.getBy_();
        Integer start_ = pageReq.getStart_();
        Integer count_ = pageReq.getCount_();
        String order_ = pageReq.getOrder_();
        QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
        SortBuilder sortBuilder = null;
        if (StringUtils.isNoneEmpty(new CharSequence[]{by_}) && StringUtils.isNoneEmpty(new CharSequence[]{order_})) {
            if ("asc".equalsIgnoreCase(by_)) {
                sortBuilder = SortBuilders.fieldSort(order_).order(SortOrder.ASC);
            } else {
                sortBuilder = SortBuilders.fieldSort(order_).order(SortOrder.DESC);
            }
        }

        PageRes pageRes_ES;
        try {
            SearchResponse searchResponse = this.elasticSearchRestClient.getDocs(indexsInfoVO.getIndex(), queryBuilder, sortBuilder, (SearchField)null, start_, count_);
            pageRes_ES = this.getPaginationResponse(searchResponse);
            return pageRes_ES;
        } catch (ElasticSearchException var12) {
            logger.error("分页查询异常：{}", var12);
            pageRes_ES = new PageRes();
            pageRes_ES.setTotal(0L);
            pageRes_ES.setList(new ArrayList());
            pageRes_ES.setMessage(var12.getMessage());
            pageRes_ES.setCode(ResultCodeEnum.UNKNOW_FAILED.getCode().toString());
            return pageRes_ES;
        }
    }

    public List<Map<String, Object>> queryStatistics(IndexsInfoVO indexsInfoVO, List<QueryCondition> conditions, SearchField field) {
        String[] indexs = indexsInfoVO.getIndex();
        if (indexs != null && indexs.length != 0) {
            String[] var5 = indexs;
            int var6 = indexs.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String index = var5[var7];
                Boolean index_status = this.elasticSearchRestClient.checkEsIndexState(index);
                Boolean existEsIndex = this.elasticSearchRestClient.isExistEsIndex(index);
                if (!existEsIndex || !index_status) {
                    logger.info(index + "索引不存在，请检查");
                    return new ArrayList();
                }
            }

            try {
                QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
                SearchResponse searchResponse = null;
                searchResponse = this.elasticSearchRestClient.getDocs(indexsInfoVO.getIndex(), queryBuilder, (SortBuilder)null, field, 0, 1);
                String fieldName = "aggs" + field.getFieldName();
                Aggregation aggregation = searchResponse.getAggregations().get(fieldName);
                List<Map<String, Object>> list = ElasticSearchUtil.getMultiBucketsMap(field, aggregation);
                return list;
            } catch (ElasticSearchException var11) {
                logger.error("多索引聚合查询异常：{}", var11);
                return new ArrayList();
            }
        } else {
            logger.info("未匹配到任何索引，无法查询数据");
            return new ArrayList();
        }
    }

    public List<Map<String, Object>> queryStatistics(List<QueryCondition> conditions, SearchField field) {
        try {
            String indexName = this.getIndexName();
            String[] indexArr = this.getIndexListByBaseIndexName(indexName);
            if (indexArr != null && indexArr.length != 0) {
                QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
                SearchResponse searchResponse = null;
                searchResponse = this.elasticSearchRestClient.getDocs(indexArr, queryBuilder, (SortBuilder)null, field, 0, 1);
                String fieldName = "aggs" + field.getFieldName();
                Aggregation aggregation = searchResponse.getAggregations().get(fieldName);
                logger.debug("聚合查询数据：" + this.gson.toJson(aggregation));
                List<Map<String, Object>> list = ElasticSearchUtil.getMultiBucketsMap(field, aggregation);
                return list;
            } else {
                return new ArrayList();
            }
        } catch (ElasticsearchStatusException var10) {
            RestStatus status = var10.status();
            if (status.getStatus() != 404) {
                logger.error("查询出现异常", var10);
                throw var10;
            } else {
                return new ArrayList();
            }
        } catch (Exception var11) {
            logger.error("查询出现异常", var11);
            return new ArrayList();
        }
    }

    public String[] getIndexListByBaseIndexName(String baseIndexName) {
        if (!StringUtils.isNotEmpty(baseIndexName)) {
            String[] allIndex = this.elasticSearchRestClient.getAllIndex();
            return allIndex;
        } else if (!baseIndexName.endsWith("*") && !baseIndexName.startsWith("*")) {
            return new String[]{baseIndexName};
        } else {
            String indexName = baseIndexName.replace("*", "");
            String[] allIndex = this.elasticSearchRestClient.getAllIndex();
            List<String> list = new ArrayList();
            String[] array = allIndex;
            int var6 = allIndex.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String index = array[var7];
                if (index.contains(indexName) || index.contains(indexName.toLowerCase())) {
                    list.add(index);
                }
            }

            array = new String[list.size()];
            String[] index_array = (String[])list.toArray(array);
            return index_array;
        }
    }

    private List<T> getResultList(SearchResponse searchResponse) {
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ElasticSearchVO<T> elasticSearchVO = (ElasticSearchVO)gson.fromJson(searchResponse.toString(), (new TypeToken<ElasticSearchVO<T>>() {
        }).getType());
        List<T> list = elasticSearchVO.getList(this.clazz, this.idField);
        return list;
    }

    private PageRes<T> getPaginationResponse(SearchResponse searchResponse) {
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ElasticSearchVO<T> elasticSearchVO = (ElasticSearchVO)gson.fromJson(searchResponse.toString(), (new TypeToken<ElasticSearchVO<T>>() {
        }).getType());
        PageRes<T> paginationResponse = elasticSearchVO.toPaginationResponse(this.clazz);
        return paginationResponse;
    }

    private String getIdValue(T entity) {
        String idValue = null;

        try {
            idValue = BeanUtils.getProperty(entity, this.idField);
            return idValue;
        } catch (Exception var4) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), "未配置的PrimaryKey依赖，请配置");
        }
    }

    public Serializable save(T entity) {
        return this.save(this.getIndexName(), entity);
    }

    public String checkESIndexState(String indexName) {
        String status = null;
        String[] indexList = this.getIndexListByBaseIndexName(indexName);
        String[] var4 = indexList;
        int var5 = indexList.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String index = var4[var6];
            status = this.elasticSearchRestClient.checkIndexStatus(index);
            if (status.equals("CLOSE")) {
                logger.info(index + "索引状态关闭，请检查");
                status = index;
                break;
            }
        }

        return status;
    }

    public Boolean isEsIndexExist(String indexName) {
        Boolean result = false;
        result = this.elasticSearchRestClient.checkEsIndexState(indexName);
        return result;
    }

    public Serializable save(String indexName, T entity) {
        String createDoc = this.savedoc(indexName, entity);
        return createDoc;
    }

    private void refreshIndex(String indexName) {
        this.elasticSearchRestClient.refreshIndexByIndexName(indexName);
    }

    private String savedoc(String indexName, T entity) {
        String id = this.getIdValue(entity);
        Map<String, Object> map = ElasticSearchUtil.transBean2Map(entity);
        String createDoc = this.elasticSearchRestClient.createDoc(indexName, id, map);
        return createDoc;
    }

    private Boolean createIndex(int shardCount, int repliceCount, T entity) {
        String indexName = this.getIndexName();
        Boolean result = this.elasticSearchRestClient.createEsIndex(indexName, shardCount, repliceCount, entity.getClass());
        return result;
    }

    private Boolean createIndex(String indexName, int shardCount, int repliceCount, T entity) {
        Boolean result = this.elasticSearchRestClient.createEsIndex(indexName, shardCount, repliceCount, entity.getClass());
        return result;
    }

    private void judgeIndexStatus(String indexName, T entity) {
        if (Boolean.FALSE.equals(this.isEsIndexExist(indexName))) {
            Boolean createIndex = this.createIndex(indexName, this.shardCount, this.repliceCount, entity);
            logger.info("索引创建状态结果：" + createIndex);
        }

        String checkESIndexState = this.checkESIndexState(indexName);
        if (!checkESIndexState.equals("OPEN")) {
            Boolean openIndexByIndexName = this.elasticSearchRestClient.openIndexByIndexName(checkESIndexState);
            logger.info("索引开启状态结果：" + openIndexByIndexName);
        }

    }

    public void addList(String indexName, List<T> entities) {
        if (entities != null && !entities.isEmpty()) {
            this.bulkDoc(indexName, entities);
        }
    }

    public void addList(List<T> entities) {
        this.addList(this.getIndexName(), entities);
    }

    private void bulkDoc(String indexName, List<T> entities) {
        List<EsDocVO> list = new ArrayList();
        Iterator var4 = entities.iterator();

        while(var4.hasNext()) {
            T entity = (T)var4.next();
            EsDocVO esDocVO = new EsDocVO();
            String idValue = this.getIdValue(entity);
            Map<String, Object> map = ElasticSearchUtil.transBean2Map(entity);
            esDocVO.setIdValue(idValue);
            esDocVO.setMap(map);
            list.add(esDocVO);
        }

        String bulkCreateDocs = this.elasticSearchRestClient.bulkCreateDocs(indexName, list);
        logger.debug("bulk Message info:" + bulkCreateDocs);
    }

    public boolean delIndexByIndexName(String indexName) {
        boolean result = this.elasticSearchRestClient.delIndexByIndexName(indexName);
        return result;
    }

    public boolean addAlias(String indexName, String alias) {
        Boolean addAlias = this.elasticSearchRestClient.addAlias(indexName, alias);
        return addAlias;
    }

    private ScrollVO<T> getScrollResultList(SearchResponse searchResponse) {
        ScrollVO<T> scroll = new ScrollVO();
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ElasticSearchVO<T> elasticSearchVO = (ElasticSearchVO)gson.fromJson(searchResponse.toString(), (new TypeToken<ElasticSearchVO<T>>() {
        }).getType());
        List<T> list = elasticSearchVO.getList(this.clazz, this.idField);
        String _scroll_id = elasticSearchVO.get_scroll_id();
        long total = elasticSearchVO.getHits().getTotal().getValue();
        scroll.setList(list);
        scroll.setScrollId(_scroll_id);
        scroll.setTotal(total);
        return scroll;
    }

    public ScrollVO<T> findAll(IndexsInfoVO indexsInfoVO, List<QueryCondition> conditions, String value, String sort, Integer size) {
        QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
        SortBuilder sortBuilder = null;
        if (StringUtils.isNoneEmpty(new CharSequence[]{value}) && StringUtils.isNoneEmpty(new CharSequence[]{sort})) {
            if ("asc".equalsIgnoreCase(sort)) {
                sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.ASC);
            } else {
                sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.DESC);
            }
        }

        try {
            SearchResponse searchResponse = this.elasticSearchRestClient.getDocsByScroll(indexsInfoVO.getIndex(), queryBuilder, sortBuilder, (SearchField)null, size);
            ScrollVO<T> scrollVO = this.getScrollResultList(searchResponse);
            return scrollVO;
        } catch (ElasticSearchException var10) {
            logger.error("游标分页查询异常：{}", var10);
            return new ScrollVO();
        }
    }

    public ScrollVO<T> findAll(List<QueryCondition> conditions, String value, String sort, Integer size) {
        String indexName = this.getIndexName();
        String[] indexNameArr = this.getIndexListByBaseIndexName(indexName);
        QueryBuilder queryBuilder = ElasticSearchUtil.toQueryBuilder(conditions);
        SortBuilder sortBuilder = null;
        if (StringUtils.isNoneEmpty(new CharSequence[]{value}) && StringUtils.isNoneEmpty(new CharSequence[]{sort})) {
            if ("asc".equalsIgnoreCase(sort)) {
                sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.ASC);
            } else {
                sortBuilder = SortBuilders.fieldSort(value).order(SortOrder.DESC);
            }
        }

        SearchResponse searchResponse = this.elasticSearchRestClient.getDocsByScroll(indexNameArr, queryBuilder, sortBuilder, (SearchField)null, size);
        ScrollVO<T> scrollVO = this.getScrollResultList(searchResponse);
        return scrollVO;
    }

    public ScrollVO<T> searchByScrollId(String scrollId) {
        SearchResponse searchResponse = this.elasticSearchRestClient.searchByScrollId(scrollId);
        ScrollVO<T> scrollVO = this.getScrollResultList(searchResponse);
        return scrollVO;
    }

    public Boolean cleanScrollId(String scrollId) {
        boolean result = this.elasticSearchRestClient.clearScroll(scrollId);
        return result;
    }

    public void deleteList(List<T> entities) {
        try {
            Iterator var2 = entities.iterator();

            while(var2.hasNext()) {
                String idValue = this.getIdValue((T)var2.next());
                this.deleteById(idValue);
            }
        } catch (Exception var5) {
            logger.error("请检查索引是否存在或状态", var5);
        }

    }

    public void deleteList(String indexName, List<T> entities) {
        if (this.isEsIndexExist(indexName) && this.checkESIndexState(indexName).equals("OPEN")) {
            Iterator var3 = entities.iterator();

            while(var3.hasNext()) {
                String idValue = this.getIdValue((T)var3.next());
                this.deleteById(indexName, idValue);
            }
        } else {
            logger.info("请检查索引是否存在或状态");
        }

    }

    public void deleteById(String indexName, Serializable id) {
        if (this.isEsIndexExist(indexName) && this.checkESIndexState(indexName).equals("OPEN")) {
            Boolean result = this.elasticSearchRestClient.delDocByIndexName(indexName, id.toString());
            logger.info("result:" + result);
        } else {
            logger.info("请检查索引是否存在或状态");
        }

    }

    public void deleteById(Serializable id) {
        try {
            String indexName = this.getIndexName();
            Boolean result = this.elasticSearchRestClient.delDocByIndexName(indexName, id.toString());
            logger.info("result:" + result);
        } catch (Exception var4) {
            logger.error("请检查索引是否存在或状态", var4);
        }

    }
}

