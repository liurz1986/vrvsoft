package cn.com.liurz.jpa.util;

import cn.com.liurz.jpa.condition.QueryCondition;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SpecificationUtil {
    private SpecificationUtil() {
    }

    public static <T> Specification<T> c2s(final List<QueryCondition> conditions) {
        Specification<T> result = new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate[] predicates = new Predicate[conditions.size()];

                for(int i = 0; i < conditions.size(); ++i) {
                    QueryCondition queryCondition = (QueryCondition)conditions.get(i);
                    Specification<T> c2s = queryCondition.c2s();
                    predicates[i] = c2s.toPredicate(root, query, cb);
                }

                Predicate and = cb.and(predicates);
                return and;
            }
        };
        return result;
    }

    /** @deprecated */
    @Deprecated
    public static <T> Specification<T> c2s(QueryCondition condition) {
        Specification<T> c2s = condition.c2s();
        return c2s;
    }

    @SafeVarargs
    public static <T> Specification<T> and(final Specification<T>... specis) {
        final int length = specis.length;
        Specification<T> result = new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate[] predicates = new Predicate[length];

                for(int i = 0; i < length; ++i) {
                    predicates[i] = specis[i].toPredicate(root, query, cb);
                }

                return cb.and(predicates);
            }
        };
        return result;
    }
}
