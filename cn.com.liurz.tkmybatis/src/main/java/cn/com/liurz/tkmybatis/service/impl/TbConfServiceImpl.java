package cn.com.liurz.tkmybatis.service.impl;

import cn.com.liurz.tkmybatis.entity.TbConf;
import cn.com.liurz.tkmybatis.mapper.TbConfMapper;
import cn.com.liurz.tkmybatis.service.TbConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TbConfServiceImpl extends BaseServiceImpl<TbConf> implements TbConfService {
    @Autowired
    private TbConfMapper tbConfMapper;

    @Override
    public List<TbConf> findByCondition(TbConf tbConf) {
        Example example = new Example(TbConf.class);
        example.createCriteria().andEqualTo("confId",tbConf.getConfId());
        return  this.findByExample(example);
    }

    @Override
    public List<TbConf> getAll() {
        return tbConfMapper.selectAll();
    }

    @Override
    public TbConf getOneByConfId(String confId) {
        return this.findById(confId);
    }

    @Override
    public void batchSaves(List<TbConf> list) {
        this.save(list);
    }

}
