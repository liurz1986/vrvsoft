package cn.com.liurz.jpa.condition;

import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class QueryCondition implements ICondition {
    private ICondition inner;

    public QueryCondition() {
    }

    public static QueryCondition withInner(ICondition inner) {
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.inner = inner;
        return queryCondition;
    }

    public QueryCondition copy() {
        QueryCondition nCondition = new QueryCondition();
        nCondition.inner = this.inner;
        return nCondition;
    }

    public static QueryCondition eq(String field, Object value) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition eq = SingleCondition.eq(field, value);
        queryCondition.inner = eq;
        return queryCondition;
    }

    public static QueryCondition eq(String field, Object value, boolean isNum) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition eq = SingleCondition.eq(field, value);
        queryCondition.inner = eq;
        return queryCondition;
    }

    public static QueryCondition notEq(String field, Object value) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition eq = SingleCondition.notEq(field, value);
        queryCondition.inner = eq;
        return queryCondition;
    }

    public static QueryCondition notEq(String field, Object value, boolean isNum) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition eq = SingleCondition.notEq(field, value);
        queryCondition.inner = eq;
        return queryCondition;
    }

    public static QueryCondition in(String field, Collection<?> values) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition in = CollectionCondition.in(field, values);
        queryCondition.inner = in;
        return queryCondition;
    }

    public static QueryCondition in(String field, Collection<?> values, boolean isNum) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition in = CollectionCondition.in(field, values);
        queryCondition.inner = in;
        return queryCondition;
    }

    public static QueryCondition in(String field, String[] values) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition in = CollectionCondition.in(field, Arrays.asList(values));
        queryCondition.inner = in;
        return queryCondition;
    }

    public static QueryCondition in(String field, String[] values, boolean isNum) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition in = CollectionCondition.in(field, Arrays.asList(values));
        queryCondition.inner = in;
        return queryCondition;
    }

    public static <Y extends Comparable<? super Y>> QueryCondition between(String field, Y value1, Y value2) {
        QueryCondition queryCondition = new QueryCondition();
        BetweenCondition<Y> betweenCondition = new BetweenCondition(field, value1, value2);
        queryCondition.inner = betweenCondition;
        return queryCondition;
    }

    public static <Y extends Comparable<? super Y>> QueryCondition between(String field, Y value1, Y value2, boolean isNum) {
        QueryCondition queryCondition = new QueryCondition();
        BetweenCondition<Y> betweenCondition = new BetweenCondition(field, value1, value2);
        queryCondition.inner = betweenCondition;
        return queryCondition;
    }

    public static QueryCondition notNull(String field) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition notNull = SingleCondition.isNotNull(field);
        queryCondition.inner = notNull;
        return queryCondition;
    }

    public static QueryCondition isNull(String field) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition con = SingleCondition.isNull(field);
        queryCondition.inner = con;
        return queryCondition;
    }

    public static QueryCondition like(String field, String str) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition like = SingleCondition.like(field, str);
        queryCondition.inner = like;
        return queryCondition;
    }

    public static QueryCondition likeEnd(String field, String str) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition like = SingleCondition.likeEnd(field, str);
        queryCondition.inner = like;
        return queryCondition;
    }

    public static QueryCondition likeBegin(String field, String str) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition like = SingleCondition.likeBegin(field, str);
        queryCondition.inner = like;
        return queryCondition;
    }

    public static <T extends Comparable<? super T>> QueryCondition gt(String field, T value) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition gt = ComparableCondition.gt(field, value);
        queryCondition.inner = gt;
        return queryCondition;
    }

    public static <T extends Comparable<? super T>> QueryCondition ge(String field, T value) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition ge = ComparableCondition.ge(field, value);
        queryCondition.inner = ge;
        return queryCondition;
    }

    public static <T extends Comparable<? super T>> QueryCondition le(String field, T value) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition le = ComparableCondition.le(field, value);
        queryCondition.inner = le;
        return queryCondition;
    }

    public static <T extends Comparable<? super T>> QueryCondition lt(String field, T value) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition lt = ComparableCondition.lt(field, value);
        queryCondition.inner = lt;
        return queryCondition;
    }

    public static QueryCondition and(QueryCondition condition1, QueryCondition condition2, QueryCondition... conditions) {
        QueryCondition condition = new QueryCondition();
        ICondition and = LogicCondition.and(condition1, condition2, conditions);
        condition.inner = and;
        return condition;
    }

    public static QueryCondition or(QueryCondition condition1, QueryCondition condition2, QueryCondition... conditions) {
        QueryCondition condition = new QueryCondition();
        ICondition or = LogicCondition.or(condition1, condition2, conditions);
        condition.inner = or;
        return condition;
    }

    public static QueryCondition or(List<QueryCondition> conditions) {
        List<ICondition> conditions2 = new ArrayList();
        Iterator var2 = conditions.iterator();

        while(var2.hasNext()) {
            QueryCondition queryCondition = (QueryCondition)var2.next();
            conditions2.add(queryCondition);
        }

        QueryCondition condition = new QueryCondition();
        ICondition or = LogicCondition.or(conditions2);
        condition.inner = or;
        return condition;
    }

    public static QueryCondition not(QueryCondition condition) {
        QueryCondition queryCondition = new QueryCondition();
        ICondition not = LogicCondition.not(condition);
        queryCondition.inner = not;
        return queryCondition;
    }

    public String toString() {
        return this.inner.toStrCondition();
    }

    public String toStrCondition() {
        return this.inner.toStrCondition();
    }

    public <T> Specification<T> c2s() {
        return this.inner.c2s();
    }
}
