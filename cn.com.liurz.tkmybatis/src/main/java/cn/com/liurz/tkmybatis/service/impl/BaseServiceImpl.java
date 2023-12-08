package cn.com.liurz.tkmybatis.service.impl;

import cn.com.liurz.tkmybatis.mapper.BaseMapper;
import cn.com.liurz.tkmybatis.service.BaseService;
import cn.com.liurz.tkmybatis.util.Query;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract  class BaseServiceImpl<T> implements BaseService<T> {
    @Autowired
    protected BaseMapper<T> baseMapper;

    private Class<T> modelClass;
    public BaseServiceImpl() {
        ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
        this.modelClass = (Class)pt.getActualTypeArguments()[0];
    }
    @Override
    public T findById(String id) {
        return this.baseMapper.selectByPrimaryKey(id);
    }
    @Override
    public List<T> findAll() {
        return this.baseMapper.selectAll();
    }
    @Override
    public T findOne(T record) {
        return this.baseMapper.selectOne(record);
    }
    @Override
    public List<T> findByids(String ids) {
        return this.baseMapper.selectByIds(ids);
    }
    @Override
    public List<T> findByExample(Example example) {
        return this.baseMapper.selectByExample(example);
    }
    @Override
    public List<T> findByProperty(Class<T> entityClass, String property, Object value) {
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(property, value);
        return this.findByExample(example);
    }
    @Override
    public PageInfo<T> findPageExample(Integer page, Integer rows, Example example) {
        PageHelper.startPage(page, rows);
        List<T> list = this.findByExample(example);
        return new PageInfo(list);
    }
    @Override
    public Integer save(T record) {
        return this.baseMapper.insert(record);
    }
    @Override
    public Integer save(List<T> record) {
        return this.baseMapper.insertList(record);
    }
    @Override
    public Integer saveSelective(T record) {
        return this.baseMapper.insertSelective(record);
    }
    @Override
    public Integer update(T record) {
        return this.baseMapper.updateByPrimaryKey(record);
    }
    @Override
    public Integer updateSelective(T record) {
        return this.baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public Integer updateSelectiveByExample(T record, Example example) {
        return this.baseMapper.updateByExampleSelective(record, example);
    }
    @Override
    public Integer deleteById(Integer id) {
        return this.baseMapper.deleteByPrimaryKey(id);
    }
    @Override
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values);
        return this.baseMapper.deleteByExample(example);
    }
    @Override
    public Integer deleteByIds(String ids) {
        return this.baseMapper.deleteByIds(ids);
    }
    @Override
    public Integer count(T record) {
        return this.baseMapper.selectCount(record);
    }
    @Override
    public Integer count(Example example) {
        return this.baseMapper.selectCountByExample(example);
    }
    @Override
    public Example orderQuery(Class<?> c, Query query) {
        Example example = new Example(c);
        if (StringUtils.isNotEmpty(query.getOrder_()) && StringUtils.isNotEmpty(query.getBy_())) {
            example.setOrderByClause(query.getOrder_() + "  " + query.getBy_());
        }

        return example;
    }
}
