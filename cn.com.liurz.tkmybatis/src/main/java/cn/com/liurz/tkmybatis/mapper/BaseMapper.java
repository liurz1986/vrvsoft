package cn.com.liurz.tkmybatis.mapper;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseMapper <T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {
}
