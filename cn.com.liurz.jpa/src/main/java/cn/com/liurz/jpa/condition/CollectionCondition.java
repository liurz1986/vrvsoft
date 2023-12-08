package cn.com.liurz.jpa.condition;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import cn.com.liurz.jpa.util.CompareExpression;
import org.springframework.data.jpa.domain.Specification;

/**
 * 处理sql中 in
 * @param <T>
 */
public class CollectionCondition<T> implements ICondition {
    private String field;
    private Collection<T> value;
    private CompareExpression ex;

    public CollectionCondition() {
    }

    public CollectionCondition(String f, Collection<T> v, CompareExpression express) {
        this.field = f;
        this.value = v;
        this.ex = express;
    }

    public static <T> ICondition in(String field, Collection<T> value) {
        return new CollectionCondition(field, value, CompareExpression.In);
    }

    public ICondition copy() {
        return new CollectionCondition(this.field, this.value, this.ex);
    }

    public String toStrCondition() {
        switch(this.ex) {
            case In:
                String collect = (String)this.value.stream().map((item) -> {
                    return this.getValueStr(item);
                }).collect(Collectors.joining(",", "(", ")"));
                return this.field + " IN " + collect;
            default:
                return null;
        }
    }

    public Specification<T> c2s() {
        Specification<T> result = new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<T> fieldPath = root.get(CollectionCondition.this.field);
                switch(CollectionCondition.this.ex) {
                    case In:
                        In<T> in = cb.in(fieldPath);
                        Iterator var6 = CollectionCondition.this.value.iterator();

                        while(var6.hasNext()) {
                            in.value((T)var6.next());
                        }

                        return in;
                    default:
                        return null;
                }
            }
        };
        return result;
    }
}
