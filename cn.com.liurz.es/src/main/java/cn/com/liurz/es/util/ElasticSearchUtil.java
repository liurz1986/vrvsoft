package cn.com.liurz.es.util;

import cn.com.liurz.es.common.ElasticSearchException;
import cn.com.liurz.es.vo.SearchField;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class ElasticSearchUtil {
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);
    public static QueryBuilder toQueryBuilder(List<QueryCondition> conditions) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        QueryCondition con;
        for(Iterator var2 = conditions.iterator(); var2.hasNext(); query = query.must(toQueryBuild(con))) {
            con = (QueryCondition)var2.next();
        }

        return query;
    }

    public static QueryBuilder toQueryBuild(QueryCondition con) {
        QueryBuilder query = null;
        switch(con.getCompareExpression()) {
            case Eq:
                query = QueryBuilders.termQuery(con.getField(), con.getValue1());
                break;
            case NotEq:
                query = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(con.getField(), con.getValue1()));
                break;
            case In:
                if (con.getValue1() instanceof List) {
                    query = QueryBuilders.termsQuery(con.getField(), ((List)con.getValue1()).toArray());
                } else {
                    query = QueryBuilders.termsQuery(con.getField(), (Object[])((Object[])con.getValue1()));
                }
                break;
            case Between:
                query = QueryBuilders.rangeQuery(con.getField()).gte(con.getValue1()).lt(con.getValue2());
                break;
            case NotNull:
                query = QueryBuilders.existsQuery(con.getField());
                break;
            case IsNull:
                query = QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(con.getField()));
                break;
            case Like:
                query = QueryBuilders.wildcardQuery(con.getField(), "*" + con.getValue1() + "*");
                break;
            case LikeEnd:
                query = QueryBuilders.wildcardQuery(con.getField(), "*" + con.getValue1());
                break;
            case LikeBegin:
                query = QueryBuilders.wildcardQuery(con.getField(), con.getValue1() + "*");
                break;
            case Gt:
                query = QueryBuilders.rangeQuery(con.getField()).gt(con.getValue1());
                break;
            case Ge:
                query = QueryBuilders.rangeQuery(con.getField()).gte(con.getValue1());
                break;
            case Le:
                query = QueryBuilders.rangeQuery(con.getField()).lte(con.getValue1());
                break;
            case Lt:
                query = QueryBuilders.rangeQuery(con.getField()).lt(con.getValue1());
                break;
            case And:
                QueryBuilder must1 = toQueryBuild((QueryCondition)con.getValue1());
                QueryBuilder must2 = toQueryBuild((QueryCondition)con.getValue2());
                query = QueryBuilders.boolQuery().must(must1).must(must2);
                break;
            case Or:
                QueryBuilder queryBuilder1 = toQueryBuild((QueryCondition)con.getValue1());
                QueryBuilder queryBuilder2 = toQueryBuild((QueryCondition)con.getValue2());
                query = QueryBuilders.boolQuery().should(queryBuilder1).should(queryBuilder2);
                break;
            case Or1:
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                List<QueryCondition> conditions = (List)con.getObjs();
                Iterator var8 = conditions.iterator();

                while(var8.hasNext()) {
                    QueryCondition conditionEs = (QueryCondition)var8.next();
                    boolQueryBuilder.should(toQueryBuild(conditionEs));
                }

                query = boolQueryBuilder;
                break;
            case Not:
                query = QueryBuilders.boolQuery().mustNot(toQueryBuild((QueryCondition)con.getValue1()));
        }

        return (QueryBuilder)query;
    }

    public static Map<String, Object> transBean2Map(Object obj) {
        HashMap map = new HashMap();

        try {
            if (obj != null) {
                List<Field> allFields = getAllFields(obj);
                BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                PropertyDescriptor[] var5 = propertyDescriptors;
                int var6 = propertyDescriptors.length;

                label109:
                for(int var7 = 0; var7 < var6; ++var7) {
                    PropertyDescriptor property = var5[var7];
                    String key = property.getName();
                    Method getter = property.getReadMethod();
                    if (!getter.getReturnType().isAssignableFrom(Void.TYPE)) {
                        Object value = getter.invoke(obj);
                        if (value != null) {
                            String typeName = property.getPropertyType().getName();
                            byte var14 = -1;
                            switch(typeName.hashCode()) {
                                case -2056817302:
                                    if (typeName.equals("java.lang.Integer")) {
                                        var14 = 1;
                                    }
                                    break;
                                case -1383349348:
                                    if (typeName.equals("java.util.Map")) {
                                        var14 = 15;
                                    }
                                    break;
                                case -1325958191:
                                    if (typeName.equals("double")) {
                                        var14 = 14;
                                    }
                                    break;
                                case -530663260:
                                    if (typeName.equals("java.lang.Class")) {
                                        var14 = 18;
                                    }
                                    break;
                                case -528833112:
                                    if (typeName.equals("java.lang.FLoat")) {
                                        var14 = 5;
                                    }
                                    break;
                                case -515992664:
                                    if (typeName.equals("java.lang.Short")) {
                                        var14 = 11;
                                    }
                                    break;
                                case 104431:
                                    if (typeName.equals("int")) {
                                        var14 = 2;
                                    }
                                    break;
                                case 3039496:
                                    if (typeName.equals("byte")) {
                                        var14 = 10;
                                    }
                                    break;
                                case 3327612:
                                    if (typeName.equals("long")) {
                                        var14 = 8;
                                    }
                                    break;
                                case 64711720:
                                    if (typeName.equals("boolean")) {
                                        var14 = 4;
                                    }
                                    break;
                                case 65575278:
                                    if (typeName.equals("java.util.Date")) {
                                        var14 = 17;
                                    }
                                    break;
                                case 65821278:
                                    if (typeName.equals("java.util.List")) {
                                        var14 = 16;
                                    }
                                    break;
                                case 97526364:
                                    if (typeName.equals("float")) {
                                        var14 = 6;
                                    }
                                    break;
                                case 109413500:
                                    if (typeName.equals("short")) {
                                        var14 = 12;
                                    }
                                    break;
                                case 344809556:
                                    if (typeName.equals("java.lang.Boolean")) {
                                        var14 = 3;
                                    }
                                    break;
                                case 398507100:
                                    if (typeName.equals("java.lang.Byte")) {
                                        var14 = 9;
                                    }
                                    break;
                                case 398795216:
                                    if (typeName.equals("java.lang.Long")) {
                                        var14 = 7;
                                    }
                                    break;
                                case 761287205:
                                    if (typeName.equals("java.lang.Double")) {
                                        var14 = 13;
                                    }
                                    break;
                                case 1195259493:
                                    if (typeName.equals("java.lang.String")) {
                                        var14 = 0;
                                    }
                            }

                            switch(var14) {
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
                                case 15:
                                    map.put(key, value);
                                    break;
                                case 16:
                                    int i = 0;

                                    while(true) {
                                        if (i >= allFields.size()) {
                                            continue label109;
                                        }

                                        Field file = (Field)allFields.get(i);
                                        if (file.getName().equals(key)) {
                                            Type gt = file.getGenericType();
                                            Map<String, Object> mapParamerInfoByList = getMapParamerInfoByList(key, value, gt);
                                            map.putAll(mapParamerInfoByList);
                                            continue label109;
                                        }

                                        ++i;
                                    }
                                case 17:
                                    map.put(key, DateUtil.format((Date)value));
                                case 18:
                                    break;
                                default:
                                    map.put(key, transBean2Map(value));
                            }
                        }
                    }
                }
            }

            return map;
        } catch (Exception var19) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var19);
        }
    }
    public static List<Field> getAllFields(Object object) {
        Class clazz = object.getClass();

        ArrayList fieldList;
        for(fieldList = new ArrayList(); clazz != null; clazz = clazz.getSuperclass()) {
            fieldList.addAll(new ArrayList(Arrays.asList(clazz.getDeclaredFields())));
        }

        return fieldList;
    }
    private static Map<String, Object> getMapParamerInfoByList(String key, Object value, Type gt) {
        HashMap map = new HashMap();

        try {
            ParameterizedType pt = (ParameterizedType)gt;
            Class<?> class1 = (Class)pt.getActualTypeArguments()[0];
            String classname = class1.getName();
            if (!class1.isPrimitive() && !classname.startsWith("java.lang.")) {
                List listMap;
                ArrayList mapList;
                if ("java.util.Date".equals(classname)) {
                    listMap = (List)value;
                    mapList = new ArrayList();
                    listMap.forEach((date) -> {
                        mapList.add(DateUtil.format((Date) date));
                    });
                    map.put(key, mapList);
                } else if (class1.isAssignableFrom(Map.class)) {
                    listMap = (List)value;
                    map.put(key, listMap);
                } else {
                    listMap = (List)value;
                    mapList = new ArrayList();
                    Iterator var9 = listMap.iterator();

                    while(var9.hasNext()) {
                        Object busiObject = var9.next();
                        Map<String, Object> childrenMap = transBean2Map(busiObject);
                        mapList.add(childrenMap);
                    }

                    map.put(key, mapList);
                }
            } else {
                map.put(key, value);
            }

            return map;
        } catch (Exception var12) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var12);
        }
    }
    public static List<Map<String, Object>> getMultiBucketsMap(SearchField field, Aggregation aggregation) {
        List<Map<String, Object>> result = new ArrayList();
        switch(field.getFieldType()) {
            case NumberSum:
            case NumberAvg:
            case NumberMax:
            case NumberMin:
            case ObjectDistinctCount:
                String valueAsString = ((NumericMetricsAggregation.SingleValue)aggregation).getValueAsString();
                Map<String, Object> mapss = new HashMap();
                mapss.put(field.getFieldName(), field.getFieldType().toString());
                mapss.put("doc_count", valueAsString);
                result.add(mapss);
                break;
            case Numberstat:
                Stats stats = (Stats)aggregation;
                Map<String, Object> mapstats = new HashMap();
                double avg = stats.getAvg();
                long count = stats.getCount();
                double max = stats.getMax();
                double min = stats.getMin();
                double sum = stats.getSum();
                mapstats.put(field.getFieldName(), field.getFieldType().toString());
                mapstats.put("avg", avg);
                mapstats.put("count", count);
                mapstats.put("max", max);
                mapstats.put("min", min);
                mapstats.put("sum", sum);
                result.add(mapstats);
                break;
            default:
                MultiBucketAggregation(field, aggregation, result);
        }

        return result;
    }
    private static void MultiBucketAggregation(SearchField field, Aggregation aggregation, List<Map<String, Object>> result) {
        List<? extends MultiBucketsAggregation.Bucket> buckets = ((MultiBucketsAggregation)aggregation).getBuckets();
        Iterator var4 = buckets.iterator();

        while(true) {
            while(var4.hasNext()) {
                MultiBucketsAggregation.Bucket bucket = (MultiBucketsAggregation.Bucket)var4.next();
                Map<String, Object> maps = new HashMap();
                maps.put(field.getFieldName(), bucket.getKeyAsString());
                if (field.getChildrenField() != null && field.getChildrenField().size() > 0) {
                    Iterator var7 = field.getChildrenField().iterator();

                    while(var7.hasNext()) {
                        SearchField child = (SearchField)var7.next();
                        Aggregation childterms = bucket.getAggregations().get("aggs" + child.getFieldName());
                        List<Map<String, Object>> childResult = getMultiBucketsMap(child, childterms);
                        maps.put(child.getFieldName(), childResult);
                        result.add(maps);
                    }
                } else {
                    maps.put("doc_count", bucket.getDocCount());
                    result.add(maps);
                }
            }

            return;
        }
    }

    public static String getParamterTypeByList(Field field) {
        String typeName = null;

        try {
            Type genericType = field.getGenericType();
            ParameterizedType pt = (ParameterizedType)genericType;
            Type ts = pt.getActualTypeArguments()[0];
            typeName = ts.getTypeName();
            return typeName;
        } catch (Exception var5) {
            logger.info("泛型实例化异常", var5);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var5);
        }
    }

    public static XContentBuilder getXContentBuilder(Map<String, Class<?>> declaredFields, Object obj) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("properties");
            builder = getXContentBuilder(declaredFields, "", builder, obj);
            builder = builder.endObject().endObject();
            return builder;
        } catch (IOException var3) {
            logger.error("构造错误", var3);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var3);
        }
    }
    public static XContentBuilder getXContentBuilder(Class clazz) {
        try {
            XContentBuilder rootBuilder = XContentFactory.jsonBuilder().startObject().startObject("properties");
            rootBuilder = getClassXContentBuilder(clazz, rootBuilder);
            rootBuilder = rootBuilder.endObject().endObject();
            return rootBuilder;
        } catch (Exception var2) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var2);
        }
    }
    private static XContentBuilder getClassXContentBuilder(Class clazz, XContentBuilder rootBuilder) throws IOException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        List<Field> allFields = getAllFieldsByClass(clazz);
        Iterator var3 = allFields.iterator();

        while(var3.hasNext()) {
            Field field = (Field)var3.next();
            boolean isFinal = Modifier.isFinal(field.getModifiers());
            if (!isFinal) {
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                if (!isStatic) {
                    boolean isVolatile = Modifier.isVolatile(field.getModifiers());
                    if (!isVolatile) {
                        rootBuilder = getFieldXContentBuilder(field, rootBuilder);
                    }
                }
            }
        }

        return rootBuilder;
    }
    public static List<Field> getAllFieldsByClass(Class clazz) {
        ArrayList fieldList;
        for(fieldList = new ArrayList(); clazz != null; clazz = clazz.getSuperclass()) {
            fieldList.addAll(new ArrayList(Arrays.asList(clazz.getDeclaredFields())));
        }

        return fieldList;
    }
    private static XContentBuilder getFieldXContentBuilder(Field field, XContentBuilder rootBuilder) {
        try {
            boolean isFinal = Modifier.isFinal(field.getModifiers());
            if (isFinal) {
                return rootBuilder;
            } else {
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                if (isStatic) {
                    return rootBuilder;
                } else {
                    boolean isVolatile = Modifier.isVolatile(field.getModifiers());
                    if (isVolatile) {
                        return rootBuilder;
                    } else {
                        String name = field.getName();
                        String classname = field.getType().getName();
                        if (!field.getClass().isPrimitive() && !classname.startsWith("java.lang.") && !"java.util.Date".equals(classname)) {
                            if (!field.getType().isArray() && !field.getType().isAssignableFrom(List.class)) {
                                System.out.println(field.toString());
                                boolean isPrivate = Modifier.isPublic(field.getModifiers());
                                if (!isPrivate) {
                                }

                                rootBuilder = rootBuilder.startObject(name).startObject("properties");
                                rootBuilder = getClassXContentBuilder(field.getType(), rootBuilder);
                                rootBuilder = rootBuilder.endObject().endObject();
                            } else {
                                rootBuilder = rootBuilder.startObject(name);
                                Type ctype = getParamterTypeClasszByList(field);
                                if (!ctype.getClass().isPrimitive() && !ctype.getTypeName().startsWith("java.lang.") && !"java.util.Date".equals(classname)) {
                                    rootBuilder = rootBuilder.startObject("properties");
                                    Class<?> classz = Class.forName(ctype.getTypeName());
                                    rootBuilder = getClassXContentBuilder(classz, rootBuilder);
                                    rootBuilder = rootBuilder.endObject();
                                } else {
                                    rootBuilder = getBaseCalssXContentBuilder(ctype, rootBuilder);
                                }

                                rootBuilder = rootBuilder.endObject();
                            }
                        } else {
                            rootBuilder = rootBuilder.startObject(name);
                            rootBuilder = getBaseCalssXContentBuilder(field.getType(), rootBuilder);
                            rootBuilder = rootBuilder.endObject();
                        }

                        return rootBuilder;
                    }
                }
            }
        } catch (Exception var9) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var9);
        }
    }

    private static XContentBuilder getBaseCalssXContentBuilder(Type clazz, XContentBuilder rootBuilder) {
        try {
            String typename = clazz.getTypeName();
            byte var4 = -1;
            switch(typename.hashCode()) {
                case -2056817302:
                    if (typename.equals("java.lang.Integer")) {
                        var4 = 1;
                    }
                    break;
                case -1325958191:
                    if (typename.equals("double")) {
                        var4 = 12;
                    }
                    break;
                case -530663260:
                    if (typename.equals("java.lang.Class")) {
                        var4 = 0;
                    }
                    break;
                case -527879800:
                    if (typename.equals("java.lang.Float")) {
                        var4 = 15;
                    }
                    break;
                case -515992664:
                    if (typename.equals("java.lang.Short")) {
                        var4 = 11;
                    }
                    break;
                case 104431:
                    if (typename.equals("int")) {
                        var4 = 2;
                    }
                    break;
                case 3039496:
                    if (typename.equals("byte")) {
                        var4 = 8;
                    }
                    break;
                case 3327612:
                    if (typename.equals("long")) {
                        var4 = 6;
                    }
                    break;
                case 64711720:
                    if (typename.equals("boolean")) {
                        var4 = 4;
                    }
                    break;
                case 65575278:
                    if (typename.equals("java.util.Date")) {
                        var4 = 3;
                    }
                    break;
                case 97526364:
                    if (typename.equals("float")) {
                        var4 = 14;
                    }
                    break;
                case 109413500:
                    if (typename.equals("short")) {
                        var4 = 10;
                    }
                    break;
                case 344809556:
                    if (typename.equals("java.lang.Boolean")) {
                        var4 = 5;
                    }
                    break;
                case 398507100:
                    if (typename.equals("java.lang.Byte")) {
                        var4 = 9;
                    }
                    break;
                case 398795216:
                    if (typename.equals("java.lang.Long")) {
                        var4 = 7;
                    }
                    break;
                case 761287205:
                    if (typename.equals("java.lang.Double")) {
                        var4 = 13;
                    }
                    break;
                case 1195259493:
                    if (typename.equals("java.lang.String")) {
                        var4 = 16;
                    }
            }

            switch(var4) {
                case 0:
                    break;
                case 1:
                case 2:
                    rootBuilder = rootBuilder.field("type", "integer");
                    break;
                case 3:
                    rootBuilder = rootBuilder.field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss");
                    break;
                case 4:
                case 5:
                    rootBuilder = rootBuilder.field("type", "boolean");
                    break;
                case 6:
                case 7:
                    rootBuilder = rootBuilder.field("type", "long");
                    break;
                case 8:
                case 9:
                    rootBuilder = rootBuilder.field("type", "byte");
                    break;
                case 10:
                case 11:
                    rootBuilder = rootBuilder.field("type", "short");
                    break;
                case 12:
                case 13:
                    rootBuilder = rootBuilder.field("type", "double");
                    break;
                case 14:
                case 15:
                    rootBuilder = rootBuilder.field("type", "float");
                    break;
                case 16:
                    rootBuilder = rootBuilder.field("type", "keyword");
                    break;
                default:
                    if (clazz.getClass().isPrimitive()) {
                        rootBuilder = rootBuilder.field("type", "keyword");
                    }
            }

            return rootBuilder;
        } catch (Exception var5) {
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var5);
        }
    }

    public static Type getParamterTypeClasszByList(Field field) {
        try {
            Type genericType = field.getGenericType();
            ParameterizedType pt = (ParameterizedType)genericType;
            Type ts = pt.getActualTypeArguments()[0];
            return ts;
        } catch (Exception var4) {
            logger.info("泛型实例化异常", var4);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var4);
        }
    }

    private static XContentBuilder getXContentBuilder(Map<String, Class<?>> declaredFields, String rootName, XContentBuilder rootBuilder, Object obj) {
        try {
            Iterator var4 = declaredFields.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, Class<?>> field = (Map.Entry)var4.next();
                Class<?> type = (Class)field.getValue();
                String typename = type.getName();
                String name = (String)field.getKey();
                if (!StringUtils.isEmpty(rootName)) {
                    name = rootName + "." + name;
                }

                logger.info("名称:" + name + ",类型:" + typename);
                byte var10 = -1;
                switch(typename.hashCode()) {
                    case -2056817302:
                        if (typename.equals("java.lang.Integer")) {
                            var10 = 2;
                        }
                        break;
                    case -1325958191:
                        if (typename.equals("double")) {
                            var10 = 13;
                        }
                        break;
                    case -527879800:
                        if (typename.equals("java.lang.Float")) {
                            var10 = 16;
                        }
                        break;
                    case -515992664:
                        if (typename.equals("java.lang.Short")) {
                            var10 = 12;
                        }
                        break;
                    case 104431:
                        if (typename.equals("int")) {
                            var10 = 3;
                        }
                        break;
                    case 3039496:
                        if (typename.equals("byte")) {
                            var10 = 9;
                        }
                        break;
                    case 3327612:
                        if (typename.equals("long")) {
                            var10 = 7;
                        }
                        break;
                    case 64711720:
                        if (typename.equals("boolean")) {
                            var10 = 5;
                        }
                        break;
                    case 65575278:
                        if (typename.equals("java.util.Date")) {
                            var10 = 4;
                        }
                        break;
                    case 65821278:
                        if (typename.equals("java.util.List")) {
                            var10 = 17;
                        }
                        break;
                    case 97526364:
                        if (typename.equals("float")) {
                            var10 = 15;
                        }
                        break;
                    case 109413500:
                        if (typename.equals("short")) {
                            var10 = 11;
                        }
                        break;
                    case 344809556:
                        if (typename.equals("java.lang.Boolean")) {
                            var10 = 6;
                        }
                        break;
                    case 392722245:
                        if (typename.equals("[Ljava.lang.String;")) {
                            var10 = 1;
                        }
                        break;
                    case 398507100:
                        if (typename.equals("java.lang.Byte")) {
                            var10 = 10;
                        }
                        break;
                    case 398795216:
                        if (typename.equals("java.lang.Long")) {
                            var10 = 8;
                        }
                        break;
                    case 761287205:
                        if (typename.equals("java.lang.Double")) {
                            var10 = 14;
                        }
                        break;
                    case 1195259493:
                        if (typename.equals("java.lang.String")) {
                            var10 = 0;
                        }
                }

                switch(var10) {
                    case 0:
                        rootBuilder = rootBuilder.startObject(name).field("type", "keyword").endObject();
                        break;
                    case 1:
                        rootBuilder = rootBuilder.startArray(name).endArray();
                        break;
                    case 2:
                    case 3:
                        rootBuilder = rootBuilder.startObject(name).field("type", "integer").endObject();
                        break;
                    case 4:
                        logger.info("java.util.Date 日期:" + name);
                        rootBuilder = rootBuilder.startObject(name).field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject();
                        break;
                    case 5:
                    case 6:
                        rootBuilder = rootBuilder.startObject(name).field("type", "boolean").endObject();
                        break;
                    case 7:
                    case 8:
                        rootBuilder = rootBuilder.startObject(name).field("type", "long").endObject();
                        break;
                    case 9:
                    case 10:
                        rootBuilder = rootBuilder.startObject(name).field("type", "long").endObject();
                        break;
                    case 11:
                    case 12:
                        rootBuilder = rootBuilder.startObject(name).field("type", "short").endObject();
                        break;
                    case 13:
                    case 14:
                        rootBuilder = rootBuilder.startObject(name).field("type", "double").endObject();
                        break;
                    case 15:
                    case 16:
                        rootBuilder = rootBuilder.startObject(name).field("type", "float").endObject();
                        break;
                    case 17:
                        rootBuilder = getListXContentBuilder(rootBuilder, obj, name);
                        break;
                    default:
                        Field[] fields = type.newInstance().getClass().getDeclaredFields();
                        Map<String, Class<?>> map = fieldsConvertMap(fields);
                        rootBuilder = getXContentBuilder(map, name, rootBuilder, obj);
                }
            }

            return rootBuilder;
        } catch (Exception var13) {
            logger.error("解析匹配错误", var13);
            throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), var13);
        }
    }


    private static XContentBuilder getListXContentBuilder(XContentBuilder rootBuilder, Object obj, String name) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Field declaredField = obj.getClass().getDeclaredField(name);
        String paramterType = getParamterTypeByList(declaredField);
        if (paramterType.contains("java.util.Map")) {
            Map<String, Class<?>> maps = new HashMap();
            declaredField.setAccessible(true);
            List<Map<String, Object>> listMap = (List)declaredField.get(obj);
            Iterator var7 = listMap.iterator();

            while(var7.hasNext()) {
                Map<String, Object> map = (Map)var7.next();
                Map<String, Class<?>> fieldMap = getFieldMap(map, name);
                maps.putAll(fieldMap);
            }

            rootBuilder = getXContentBuilder(maps, (String)null, rootBuilder, obj);
        } else {
            Class<?> forName = Class.forName(paramterType);
            Map<String, Class<?>> mapList = new HashMap();
            mapList.put(name, forName);
            rootBuilder = getXContentBuilder(mapList, (String)null, rootBuilder, obj);
        }

        return rootBuilder;
    }

    public static Map<String, Class<?>> getFieldMap(Map<String, Object> map, String rootName) {
        Map<String, Class<?>> field = new HashMap();
        Iterator var3 = map.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var3.next();
            String key = (String)entry.getKey();
            if (!StringUtils.isEmpty(rootName)) {
                key = rootName + "." + key;
            }

            Object value = entry.getValue();
            if (value != null) {
                String name = value.getClass().getName();
                byte var9 = -1;
                switch(name.hashCode()) {
                    case -1402722386:
                        if (name.equals("java.util.HashMap")) {
                            var9 = 1;
                        }
                        break;
                    case -1383349348:
                        if (name.equals("java.util.Map")) {
                            var9 = 0;
                        }
                }

                switch(var9) {
                    case 0:
                    case 1:
                        Map<String, Object> maps = (Map)value;
                        field.putAll(getFieldMap(maps, key));
                        break;
                    default:
                        Class<? extends Object> fieldClass = entry.getValue().getClass();
                        field.put(key, fieldClass);
                }
            }
        }

        return field;
    }

    public static Map<String, Class<?>> fieldsConvertMap(Field[] fields) {
        Map<String, Class<?>> map = new HashMap();
        Field[] var2 = fields;
        int var3 = fields.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            map.put(field.getName(), field.getType());
        }

        return map;
    }


}
