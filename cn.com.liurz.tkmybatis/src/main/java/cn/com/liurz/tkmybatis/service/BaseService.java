package cn.com.liurz.tkmybatis.service;

import cn.com.liurz.tkmybatis.util.Query;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface BaseService<T> {
    Integer save(T var1);

    Integer save(List<T> var1);

    T findById(String var1);

    List<T> findAll();

    T findOne(T var1);

    List<T> findByids(String var1);

    List<T> findByExample(Example var1);

    List<T> findByProperty(Class<T> var1, String var2, Object var3);

    PageInfo<T> findPageExample(Integer var1, Integer var2, Example var3);

    Integer saveSelective(T var1);

    Integer update(T var1);

    Integer updateSelective(T var1);

    Integer updateSelectiveByExample(T var1, Example var2);

    Integer deleteById(Integer var1);

    Integer deleteByIds(Class<T> clazz, String property, List<Object> values);

    Integer deleteByIds(String var1);

    Integer count(T var1);

    Integer count(Example var1);

    Example orderQuery(Class<?> var1, Query var2);
}
