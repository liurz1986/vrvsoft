package cn.com.liurz.jpa.common;


import cn.com.liurz.jpa.condition.QueryCondition;
import cn.com.liurz.jpa.util.SpecificationUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
    public BaseServiceImpl() {
    }

    public <S extends T> S save(S entity) {
        return this.getRepository().save(entity);
    }

    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        return this.getRepository().saveAll(entities);
    }

    public void delete(ID id) {
        this.getRepository().deleteById(id);
    }

    public void delete(T entity) {
        this.getRepository().delete(entity);
    }

    public void deleteInBatch(Iterable<T> entities) {
        this.getRepository().deleteInBatch(entities);
    }

    public void deleteAllInBatch() {
        this.getRepository().deleteAllInBatch();
    }

    public T getOne(ID id) {
        Optional<T> findById = this.getRepository().findById(id);
        return findById.isPresent() ? findById.get() : null;
    }

    public boolean exists(ID id) {
        return this.getRepository().existsById(id);
    }

    public boolean exists(T example) {
        ExampleMatcher match = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<T> of = Example.of(example, match);
        return this.exists(of);
    }

    public boolean exists(Example<T> example) {
        return this.getRepository().exists(example);
    }

    public long count() {
        return this.getRepository().count();
    }

    public long count(T example) {
        ExampleMatcher match = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<T> of = Example.of(example, match);
        return this.count(of);
    }

    public <S extends T> long count(Example<S> example) {
        return this.getRepository().count(example);
    }

    public List<T> findAll() {
        return this.getRepository().findAll();
    }

    public List<T> findAll(Sort sort) {
        return this.getRepository().findAll(sort);
    }

    public List<T> findAll(Iterable<ID> ids) {
        return this.getRepository().findAllById(ids);
    }

    public List<T> findAll(T example) {
        ExampleMatcher match = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<T> of = Example.of(example, match);
        return this.findAll(of);
    }

    public <S extends T> List<S> findAll(Example<S> example) {
        return this.getRepository().findAll(example);
    }

    public <S extends T> List<S> findAll(S example, Sort sort) {
        ExampleMatcher match = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<S> of = Example.of(example, match);
        return this.findAll(of, sort);
    }

    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return this.getRepository().findAll(example, sort);
    }

    public Page<T> findAll(Pageable pageable) {
        return this.getRepository().findAll(pageable);
    }

    public <S extends T> Page<S> findAll(S example, Pageable pageable) {
        ExampleMatcher match = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<S> of = Example.of(example, match);
        return this.findAll(of, pageable);
    }

    public Page<T> findAll(T example, List<QueryCondition> conditions, Pageable pageable) {
        ExampleMatcher match = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        final Example<T> of = Example.of(example, match);
        final Specification<T> c2s = SpecificationUtil.c2s(conditions);
        Specification<T> result = new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = QueryByExamplePredicateBuilder.getPredicate(root, cb, of);
                Predicate and = cb.and(predicate, c2s.toPredicate(root, query, cb));
                return and;
            }
        };
        return this.getRepository().findAll(result, pageable);
    }

    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return this.getRepository().findAll(example, pageable);
    }

    public boolean exists(List<QueryCondition> conditions) {
        return this.count(conditions) > 0L;
    }

    public long count(List<QueryCondition> conditions) {
        Specification<T> c2s = SpecificationUtil.c2s(conditions);
        return this.getRepository().count(c2s);
    }

    public List<T> findAll(List<QueryCondition> conditions) {
        Specification<T> c2s = SpecificationUtil.c2s(conditions);
        return this.getRepository().findAll(c2s);
    }

    public List<T> findAll(List<QueryCondition> conditions, Sort sort) {
        Specification<T> c2s = SpecificationUtil.c2s(conditions);
        return this.getRepository().findAll(c2s, sort);
    }

    public Page<T> findAll(List<QueryCondition> conditions, Pageable pageable) {
        Specification<T> c2s = SpecificationUtil.c2s(conditions);
        return this.getRepository().findAll(c2s, pageable);
    }
}
