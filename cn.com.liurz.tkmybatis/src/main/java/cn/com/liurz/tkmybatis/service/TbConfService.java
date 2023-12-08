package cn.com.liurz.tkmybatis.service;

import cn.com.liurz.tkmybatis.entity.TbConf;


import java.util.List;

public interface TbConfService extends BaseService<TbConf>{


    public List<TbConf> findByCondition(TbConf tbConf);

    public List<TbConf> getAll();

    public TbConf getOneByConfId(String confId);

    public void batchSaves(List<TbConf> list);
}
