package cn.com.liurz.jpa.condition;

import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cn.com.liurz.jpa.util.DateUtil;
import org.springframework.data.jpa.domain.Specification;

/**
 * 处理sql中 between---and
 * @param <T>
 */
public class BetweenCondition<T extends Comparable<? super T>> implements ICondition {
    private String f;
    private T val1;
    private T val2;

    public String getF() {
        return this.f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public T getVal1() {
        return this.val1;
    }

    public void setVal1(T val1) {
        this.val1 = val1;
    }

    public T getVal2() {
        return this.val2;
    }

    public void setVal2(T val2) {
        this.val2 = val2;
    }

    public BetweenCondition() {
    }

    public BetweenCondition(String field, T v1, T v2) {
        this.f = field;
        this.val1 = v1;
        this.val2 = v2;
    }

    public String toStrCondition() {
        if (this.val1 instanceof Date) {
            String dataStr1 = DateUtil.format((Date)this.val1);
            String dataStr2 = DateUtil.format((Date)this.val2);
            return this.f + " BETWEEN '" + dataStr1 + "' AND '" + dataStr2 + "'";
        } else {
            return this.val1 instanceof Number ? this.f + " BETWEEN " + this.val1 + " AND " + this.val2 + "" : this.f + " BETWEEN '" + this.val1 + "' AND '" + this.val2 + "'";
        }
    }

    public Specification<T> c2s() {
        Specification<T> result = new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<T> fieldPath = root.get(BetweenCondition.this.f);
                if (BetweenCondition.this.val1 instanceof Date) {
                    return cb.between(fieldPath.as(Date.class), (Date)BetweenCondition.this.val1, (Date)BetweenCondition.this.val2);
                } else if (BetweenCondition.this.val1 instanceof Number) {
                    return cb.between(fieldPath, BetweenCondition.this.val1, BetweenCondition.this.val2);
                } else {
                    return BetweenCondition.this.val1 instanceof String ? cb.between(fieldPath.as(String.class), (String)BetweenCondition.this.val1, (String)BetweenCondition.this.val2) : null;
                }
            }
        };
        return result;
    }

    public ICondition copy() {
        BetweenCondition<T> betweenCondition = new BetweenCondition(this.f, this.val1, this.val2);
        return betweenCondition;
    }
}
