package cn.com.liurz.jpa.common;

import cn.com.liurz.jpa.condition.QueryCondition;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T, ID extends Serializable> {
    BaseRepository<T, ID> getRepository();

    <S extends T> S save(S var1);

    <S extends T> Iterable<S> save(Iterable<S> var1);

    void delete(ID var1);

    void delete(T var1);

    void deleteInBatch(Iterable<T> var1);

    void deleteAllInBatch();

    T getOne(ID var1);

    boolean exists(ID var1);

    boolean exists(T var1);

    boolean exists(Example<T> var1);

    long count();

    long count(T var1);

    <S extends T> long count(Example<S> var1);

    List<T> findAll();

    List<T> findAll(Sort var1);

    List<T> findAll(Iterable<ID> var1);

    List<T> findAll(T var1);

    <S extends T> List<S> findAll(Example<S> var1);

    <S extends T> List<S> findAll(S var1, Sort var2);

    <S extends T> List<S> findAll(Example<S> var1, Sort var2);

    Page<T> findAll(Pageable var1);

    <S extends T> Page<S> findAll(S var1, Pageable var2);

    <S extends T> Page<S> findAll(Example<S> var1, Pageable var2);

    long count(List<QueryCondition> var1);

    List<T> findAll(List<QueryCondition> var1);

    List<T> findAll(List<QueryCondition> var1, Sort var2);

    Page<T> findAll(List<QueryCondition> var1, Pageable var2);

    boolean exists(List<QueryCondition> var1);

    Page<T> findAll(T var1, List<QueryCondition> var2, Pageable var3);
}
