package cn.com.liurz.es.util;

import cn.com.liurz.es.common.CompareExpression;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class QueryCondition {
    private CompareExpression compareExpression;
    private String field;
    private Class<?> fieldClazz;
    private Object value1;
    private Object value2;
    private Object objs;
    private boolean numFlag;
    private String fieldStr;

    public Object getObjs() {
        return this.objs;
    }

    public void setObjs(Object objs) {
        this.objs = objs;
    }

    private QueryCondition(CompareExpression compare, Object objs) {
        this.compareExpression = CompareExpression.Eq;
        this.numFlag = false;
        this.compareExpression = compare;
        this.objs = objs;
    }

    private QueryCondition(CompareExpression compare, String field, Object value1) {
        this(compare, field, value1, "");
    }

    private QueryCondition(CompareExpression compare, String field, Object value1, String fieldStr) {
        this(compare, field, value1, "", fieldStr);
    }

    private QueryCondition(CompareExpression compare, String field, Object value1, Object value2) {
        this.compareExpression = CompareExpression.Eq;
        this.numFlag = false;
        this.compareExpression = compare;
        this.field = field;
        this.value1 = value1;
        this.value2 = value2;
    }

    public QueryCondition(CompareExpression compare, String field, Object value1, Object value2, String fieldStr) {
        this.compareExpression = CompareExpression.Eq;
        this.numFlag = false;
        this.compareExpression = compare;
        this.field = field;
        this.value1 = value1;
        this.value2 = value2;
        this.fieldStr = fieldStr;
    }

    public QueryCondition aliasField(String fieldStr) {
        this.fieldStr = fieldStr;
        return this;
    }

    public QueryCondition copy() {
        QueryCondition nCondition = new QueryCondition(this.compareExpression, this.field, this.value1, this.value2);
        nCondition.numFlag = this.numFlag;
        return nCondition;
    }

    public static QueryCondition eq(String field, Object value) {
        return new QueryCondition(CompareExpression.Eq, field, value);
    }

    public static QueryCondition eq(String field, Object value, boolean isNum) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Eq, field, value);
        QueryCondition.setNumFlag(isNum);
        return QueryCondition;
    }

    public static QueryCondition notEq(String field, Object value) {
        return new QueryCondition(CompareExpression.NotEq, field, value);
    }

    public static QueryCondition notEq(String field, Object value, boolean isNum) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.NotEq, field, value);
        QueryCondition.setNumFlag(isNum);
        return QueryCondition;
    }

    public static QueryCondition in(String field, Collection<?> values) {
        Object[] objs = values.toArray();
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.In, field, values);
        return QueryCondition;
    }

    public static QueryCondition in(String field, Collection<?> values, boolean isNum) {
        Object[] objs = values.toArray();
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.In, field, objs);
        QueryCondition.setNumFlag(isNum);
        return QueryCondition;
    }

    public static QueryCondition in(String field, String[] values) {
        return new QueryCondition(CompareExpression.In, field, values);
    }

    public static QueryCondition in(String field, String[] values, boolean isNum) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.In, field, values);
        QueryCondition.setNumFlag(isNum);
        return QueryCondition;
    }

    public static <Y extends Comparable<? super Y>> QueryCondition between(String field, Y value1, Y value2) {
        Class<? extends Object> class1 = value1.getClass();
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Between, field, value1, value2);
        QueryCondition.setFieldClazz(class1);
        return QueryCondition;
    }

    public static <Y extends Comparable<? super Y>> QueryCondition between(String field, Y value1, Y value2, boolean isNum) {
        Class<? extends Object> class1 = value1.getClass();
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Between, field, value1, value2);
        QueryCondition.setNumFlag(isNum);
        QueryCondition.setFieldClazz(class1);
        return QueryCondition;
    }

    public static QueryCondition notNull(String field) {
        return new QueryCondition(CompareExpression.NotNull, field, "");
    }

    public static QueryCondition isNull(String field) {
        return new QueryCondition(CompareExpression.IsNull, field, "");
    }

    public static QueryCondition like(String field, String str) {
        return new QueryCondition(CompareExpression.Like, field, str);
    }

    public static QueryCondition likeEnd(String field, String str) {
        return new QueryCondition(CompareExpression.LikeEnd, field, str);
    }

    public static QueryCondition likeBegin(String field, String str) {
        return new QueryCondition(CompareExpression.LikeBegin, field, str);
    }

    public static QueryCondition gt(String field, long value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Gt, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition ge(String field, long value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Ge, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition le(String field, long value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Le, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition lt(String field, long value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Lt, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition gt(String field, int value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Gt, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition gt(String field, Date value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Gt, field, value);
        QueryCondition.setNumFlag(false);
        return QueryCondition;
    }

    public static QueryCondition gt(String field, String value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Gt, field, value);
        QueryCondition.setNumFlag(false);
        return QueryCondition;
    }

    public static QueryCondition ge(String field, Date value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Ge, field, value);
        QueryCondition.setNumFlag(false);
        return QueryCondition;
    }

    public static QueryCondition ge(String field, String value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Ge, field, value);
        QueryCondition.setNumFlag(false);
        return QueryCondition;
    }

    public static QueryCondition ge(String field, int value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Ge, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition le(String field, String value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Le, field, value);
        QueryCondition.setNumFlag(false);
        return QueryCondition;
    }

    public static QueryCondition le(String field, int value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Le, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition lt(String field, int value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Lt, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition le(String field, Date value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Le, field, value);
        QueryCondition.setNumFlag(false);
        return QueryCondition;
    }

    public static QueryCondition lt(String field, Date value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Lt, field, value);
        QueryCondition.setNumFlag(false);
        return QueryCondition;
    }

    public static QueryCondition lt(String field, BigDecimal value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Lt, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition gt(String field, BigDecimal value) {
        QueryCondition QueryCondition = new QueryCondition(CompareExpression.Gt, field, value);
        QueryCondition.setNumFlag(true);
        return QueryCondition;
    }

    public static QueryCondition and(QueryCondition condition1, QueryCondition condition2) {
        return new QueryCondition(CompareExpression.And, "", condition1, condition2);
    }

    public static QueryCondition and(QueryCondition condition1, QueryCondition condition2, QueryCondition... conditions) {
        QueryCondition condition = new QueryCondition(CompareExpression.And, "", condition1, condition2);

        for(int i = 0; i < conditions.length; ++i) {
            condition = new QueryCondition(CompareExpression.And, "", condition, conditions[i]);
        }

        return condition;
    }

    public static QueryCondition or(QueryCondition condition1, QueryCondition condition2, QueryCondition... conditions) {
        QueryCondition condition = new QueryCondition(CompareExpression.Or, "", condition1, condition2);

        for(int i = 0; i < conditions.length; ++i) {
            condition = new QueryCondition(CompareExpression.Or, "", condition, conditions[i]);
        }

        return condition;
    }

    public static QueryCondition or1(List<QueryCondition> lists) {
        QueryCondition conditionEs = new QueryCondition(CompareExpression.Or1, lists);
        return conditionEs;
    }

    public static QueryCondition or(List<QueryCondition> cons) {
        int size = cons.size();
        if (size < 2) {
            return (QueryCondition)cons.get(0);
        } else {
            QueryCondition condition = new QueryCondition(CompareExpression.Or, "", cons.get(0), cons.get(1));

            for(int i = 2; i < size; ++i) {
                condition = new QueryCondition(CompareExpression.Or, "", condition, cons.get(i));
            }

            return condition;
        }
    }

    public static QueryCondition not(QueryCondition condition) {
        return new QueryCondition(CompareExpression.Not, "", condition);
    }

    public CompareExpression getCompareExpression() {
        return this.compareExpression;
    }

    public void setCompareExpression(CompareExpression compareExpression) {
        this.compareExpression = compareExpression;
    }

    public String getField() {
        return this.field;
    }

    public String getFieldStr() {
        return StringUtils.isEmpty(this.fieldStr) ? this.field : this.fieldStr;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue1() {
        return this.value1;
    }

    public void setValue1(Object value1) {
        this.value1 = value1;
    }

    public Object getValue2() {
        return this.value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }

    public String toString() {
        switch(this.getCompareExpression()) {
            case Eq:
                return this.eqStr();
            case NotEq:
                return this.notEqStr();
            case Between:
                return this.betweenStr();
            case In:
                return this.inStr();
            case Like:
                return this.getFieldStr() + " LIKE '%" + this.getValue1().toString() + "%'";
            case LikeBegin:
                return this.getFieldStr() + " LIKE '" + this.getValue1().toString() + "%'";
            case LikeEnd:
                return this.getFieldStr() + " LIKE '%" + this.getValue1().toString() + "'";
            case Le:
                return this.leStr();
            case Lt:
                return this.ltStr();
            case Ge:
                return this.geStr();
            case Gt:
                return this.gtStr();
            case IsNull:
                return this.getFieldStr() + " IS NULL";
            case NotNull:
                return this.getFieldStr() + " IS NOT NULL";
            case And:
                return this.andStr();
            case Or:
                return this.orStr();
            case Or1:
                return this.orStr1();
            case Not:
                return this.notStr();
            default:
                return "";
        }
    }

    private String notStr() {
        QueryCondition condition5 = (QueryCondition)this.getValue1();
        return "(NOT (" + condition5.toString() + "))";
    }

    private String orStr() {
        QueryCondition condition3 = (QueryCondition)this.getValue1();
        QueryCondition condition4 = (QueryCondition)this.getValue2();
        return "((" + condition3.toString() + ") OR (" + condition4.toString() + "))";
    }

    private String orStr1() {
        List<QueryCondition> conditions = (List)this.objs;
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < conditions.size(); ++i) {
            if (i < conditions.size() - 1) {
                sb.append("(" + ((QueryCondition)conditions.get(i)).toString() + ")").append(" OR ");
            } else {
                sb.append("(" + ((QueryCondition)conditions.get(i)).toString() + ")");
            }
        }

        return "(" + sb.toString() + ")";
    }

    private String andStr() {
        QueryCondition condition1 = (QueryCondition)this.getValue1();
        QueryCondition condition2 = (QueryCondition)this.getValue2();
        return "((" + condition1.toString() + ") AND (" + condition2.toString() + "))";
    }

    private String gtStr() {
        return this.numFlag ? this.getFieldStr() + ">" + this.getValue1().toString() + "" : this.getFieldStr() + ">'" + this.getValue1().toString() + "'";
    }

    private String geStr() {
        return this.numFlag ? this.getFieldStr() + ">=" + this.getValue1().toString() + "" : this.getFieldStr() + ">='" + this.getValue1().toString() + "'";
    }

    private String ltStr() {
        return this.numFlag ? this.getFieldStr() + "<" + this.getValue1().toString() + "" : this.getFieldStr() + "<'" + this.getValue1().toString() + "'";
    }

    private String leStr() {
        return this.numFlag ? this.getFieldStr() + "<=" + this.getValue1().toString() + "" : this.getFieldStr() + "<='" + this.getValue1().toString() + "'";
    }

    private String inStr() {
        Object[] objArray = (Object[])((Object[])this.getValue1());
        String join;
        if (this.numFlag) {
            join = ArrayUtil.join(objArray, ",");
            return this.getFieldStr() + " IN (" + join + ")";
        } else {
            join = ArrayUtil.join(objArray, "','");
            return this.getFieldStr() + " IN ('" + join + "')";
        }
    }

    private String betweenStr() {
        if (this.getValue1() instanceof Date) {
            String dataStr1 = DateUtil.format((Date)this.getValue1());
            String dataStr2 = DateUtil.format((Date)this.getValue2());
            return this.getFieldStr() + " BETWEEN '" + dataStr1 + "' AND '" + dataStr2 + "'";
        } else {
            return this.numFlag ? this.getFieldStr() + " BETWEEN " + this.getValue1().toString() + " AND " + this.getValue2().toString() + "" : this.getFieldStr() + " BETWEEN '" + this.getValue1().toString() + "' AND '" + this.getValue2().toString() + "'";
        }
    }

    private String notEqStr() {
        return this.numFlag ? this.getFieldStr() + "<>" + this.getValue1().toString() + "" : this.getFieldStr() + "<>'" + this.getValue1().toString() + "'";
    }

    private String eqStr() {
        return this.numFlag ? this.getFieldStr() + "=" + this.getValue1().toString() + "" : this.getFieldStr() + "='" + this.getValue1().toString() + "'";
    }

    public boolean isNumFlag() {
        return this.numFlag;
    }

    public void setNumFlag(boolean numFlag) {
        this.numFlag = numFlag;
    }

    public Class<?> getFieldClazz() {
        return this.fieldClazz;
    }

    public void setFieldClazz(Class<?> fieldClazz) {
        this.fieldClazz = fieldClazz;
    }
}