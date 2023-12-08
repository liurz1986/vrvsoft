package cn.com.liurz.plus.mapper;

import cn.com.liurz.plus.entity.AppSysManager;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * cn.com.liurz.plus.mapper.AppSysManagerMapper
 * @author vrv
 */
@Mapper
public interface AppSysManagerMapper extends BaseMapper<AppSysManager> {

    public List<Map<String,Object>> findAll();

    public List<AppSysManager>  findAllPojo();
}
