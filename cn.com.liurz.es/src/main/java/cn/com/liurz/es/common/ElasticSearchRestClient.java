package cn.com.liurz.es.common;

import cn.com.liurz.es.vo.EsDocVO;
import cn.com.liurz.es.vo.SearchField;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ElasticSearchRestClient {
    Boolean isExistEsTypeOfIndex(String indexName, String type);

    Boolean isExistEsIndex(String index);

    Boolean createEsIndex(String indexName, int shardCount, int repliceCount, Class<?> entityClazz);

    Boolean createEsIndex(String indexName, int shardCount, int repliceCount, Map<String, Class<?>> fields, Object obj);

    SearchResponse getDocs(String[] index, QueryBuilder queryBuilder, SortBuilder sortBuilder, SearchField field, int start, int size);

    Map<String, Object> getDoc(String indexName, String id);

    String[] getAllIndex();

    Boolean checkEsIndexState(String indexName);

    String checkIndexStatus(String indexName);

    String createDoc(String indexName, String id, Map<String, Object> field);

    void refreshIndexByIndexName(String indexName);

    Boolean openIndexByIndexName(String indexName);

    String bulkCreateDocs(String indexName, List<EsDocVO> list);

    boolean delIndexByIndexName(String indexName);

    Boolean addAlias(String index, String aliasName);

    Boolean isExistIndexAlias(String index, String aliasName);

    boolean deleteExistIndexAlias(String index, String aliasName);

    Map<String, Set<AliasMetadata>> getAliasSets(String index, String aliasName);

    SearchResponse getDocsByScroll(String index, QueryBuilder queryBuilder, SortBuilder sortBuilder, SearchField field, int size);

    SearchResponse getDocsByScroll(String[] index, QueryBuilder queryBuilder, SortBuilder sortBuilder, SearchField field, int size);

    SearchResponse searchByScrollId(String scrollId);

    boolean clearScroll(String scrollId);

    Boolean delDocByIndexName(String indexName, String id);

    Settings getSetting(String indexName);

    boolean reindexTransportDate(String sourceIndexName, String destinationIndexName);
}
