package cn.com.liurz.plus.service;

import cn.com.liurz.plus.entity.AppSysManager;
import cn.com.liurz.plus.mapper.AppSysManagerMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
@Transactional
public class AppSysManagerService {
    @Autowired
    private AppSysManagerMapper appSysManagerMapper;

    public List<Map<String,Object>> getAll(){
        return appSysManagerMapper.findAll();
    }

    public List<AppSysManager>  findAllPojo(){
        return appSysManagerMapper.findAllPojo();
    }

    public AppSysManager findById(String id){
        return appSysManagerMapper.selectById(id);
    }

    /**
     * QueryWrapper封装了进行查询
     *        查询的字段为表的字段
     * @param queryFliter
     * @return
     */
    public List<AppSysManager> fliterQuery(AppSysManager queryFliter){
        QueryWrapper<AppSysManager> query = new QueryWrapper<AppSysManager>();
        query.like("app_name",queryFliter.getAppName());
        query.eq("id",queryFliter.getId());
        return appSysManagerMapper.selectList(query);
    }
}
