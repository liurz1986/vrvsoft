package cn.com.liurz.jpa.condition;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.com.liurz.jpa.util.CompareExpression;
import org.springframework.data.jpa.domain.Specification;

/**
 * 处理sql 大于、小于、大于等于、小于等于
 * @param <T>
 */
public class ComparableCondition<T extends Comparable<? super T>> implements ICondition {
    private String f;
    private T v;
    private CompareExpression ex;

    public ComparableCondition() {
    }

    public ComparableCondition(String field, T value, CompareExpression express) {
        this.f = field;
        this.v = value;
        this.ex = express;
    }

    public static <Y extends Comparable<? super Y>> ICondition gt(String field, Y value) {
        ComparableCondition<Y> comparableCondition = new ComparableCondition(field, value, CompareExpression.Gt);
        return comparableCondition;
    }

    public static <Y extends Comparable<? super Y>> ICondition ge(String field, Y value) {
        ComparableCondition<Y> comparableCondition = new ComparableCondition(field, value, CompareExpression.Ge);
        return comparableCondition;
    }

    public static <Y extends Comparable<? super Y>> ICondition le(String field, Y value) {
        ComparableCondition<Y> comparableCondition = new ComparableCondition(field, value, CompareExpression.Le);
        return comparableCondition;
    }

    public static <Y extends Comparable<? super Y>> ICondition lt(String field, Y value) {
        ComparableCondition<Y> comparableCondition = new ComparableCondition(field, value, CompareExpression.Lt);
        return comparableCondition;
    }

    public String toStrCondition() {
        String opt = "=";
        switch(this.ex) {
            case Gt:
                opt = ">";
                break;
            case Ge:
                opt = ">=";
                break;
            case Lt:
                opt = "<";
                break;
            case Le:
                opt = "<=";
        }

        return this.f + opt + this.getValueStr(this.v);
    }

    public Specification<T> c2s() {
        Specification<T> result = new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<T> fieldPath = root.get(ComparableCondition.this.f);
                switch(ComparableCondition.this.ex) {
                    case Gt:
                        return cb.greaterThan(fieldPath, ComparableCondition.this.v);
                    case Ge:
                        return cb.greaterThanOrEqualTo(fieldPath, ComparableCondition.this.v);
                    case Lt:
                        return cb.lessThan(fieldPath, ComparableCondition.this.v);
                    case Le:
                        return cb.lessThanOrEqualTo(fieldPath, ComparableCondition.this.v);
                    default:
                        return null;
                }
            }
        };
        return result;
    }

    public ICondition copy() {
        ComparableCondition<T> comparableCondition = new ComparableCondition(this.f, this.v, this.ex);
        return comparableCondition;
    }
}
