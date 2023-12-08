package cn.com.liurz.jpa.condition;

import cn.com.liurz.jpa.util.CompareExpression;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 处理slq 等于、不等于、like
 * @param <T>
 */
public class SingleCondition<T> implements ICondition {
    private String f;
    private T value;
    private CompareExpression express;

    public SingleCondition() {
    }

    public SingleCondition(String field, T value, CompareExpression ex) {
        this.f = field;
        this.value = value;
        this.express = ex;
    }

    public static <T> ICondition eq(String field, T value) {
        SingleCondition<T> singleCondition = new SingleCondition(field, value, CompareExpression.Eq);
        return singleCondition;
    }

    public static <T> ICondition notEq(String field, T value) {
        SingleCondition<T> singleCondition = new SingleCondition(field, value, CompareExpression.NotEq);
        return singleCondition;
    }

    public static ICondition isNull(String field) {
        SingleCondition<?> singleCondition = new SingleCondition(field, (Object)null, CompareExpression.IsNull);
        return singleCondition;
    }

    public static ICondition isNotNull(String field) {
        SingleCondition<?> singleCondition = new SingleCondition(field, (Object)null, CompareExpression.NotNull);
        return singleCondition;
    }

    public static ICondition like(String field, String value) {
        SingleCondition<?> singleCondition = new SingleCondition(field, value, CompareExpression.Like);
        return singleCondition;
    }

    public static ICondition likeBegin(String field, String value) {
        SingleCondition<?> singleCondition = new SingleCondition(field, value, CompareExpression.LikeBegin);
        return singleCondition;
    }

    public static ICondition likeEnd(String field, String value) {
        SingleCondition<?> singleCondition = new SingleCondition(field, value, CompareExpression.LikeEnd);
        return singleCondition;
    }

    public ICondition copy() {
        SingleCondition<T> newCon = new SingleCondition(this.f, this.value, this.express);
        return newCon;
    }

    public String toStrCondition() {
        String exStr = "=";
        switch(this.express) {
            case Eq:
                exStr = "=";
                break;
            case NotEq:
                exStr = "<>";
                break;
            case IsNull:
                return this.f + " IS NULL";
            case NotNull:
                return this.f + " IS NOT NULL";
            case Like:
                return this.f + " LIKE '%" + this.value + "%'";
            case LikeBegin:
                return this.f + " LIKE '" + this.value + "%'";
            case LikeEnd:
                return this.f + " LIKE '%" + this.value + "'";
        }

        return this.f + " " + exStr + " " + this.getValueStr(this.value);
    }

    public Specification<T> c2s() {
        Specification<T> result = new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<T> fieldPath = root.get(SingleCondition.this.f);
                switch(SingleCondition.this.express) {
                    case Eq:
                        return cb.equal(fieldPath, SingleCondition.this.value);
                    case NotEq:
                        return cb.notEqual(fieldPath, SingleCondition.this.value);
                    case IsNull:
                        return cb.isNull(fieldPath);
                    case NotNull:
                        return cb.isNotNull(fieldPath);
                    case Like:
                        return cb.like(fieldPath.as(String.class), "%" + SingleCondition.this.value + "%");
                    case LikeBegin:
                        return cb.like(fieldPath.as(String.class), SingleCondition.this.value + "%");
                    case LikeEnd:
                        return cb.like(fieldPath.as(String.class), "%" + SingleCondition.this.value);
                    default:
                        return null;
                }
            }
        };
        return result;
    }
}
