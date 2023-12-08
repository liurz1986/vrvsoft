package cn.com.liurz.flow.config;

import cn.com.liurz.flow.util.RedisCacheUtil;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义数据表主键，为了实现分布式
 */
// @Service
public class DistributedIdGenerator implements IdGenerator {
    Logger logger = LoggerFactory.getLogger(DistributedIdGenerator.class);
    public static final String idKey="activiti_id_key";

    public static final Integer initStart=1000; // 第一次没有的情况下
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Override
    public String getNextId() {
        Object data = redisCacheUtil.get(idKey);
        // 第一次没有的情况下
        if(null == data){
            logger.info("第一次没有的情况下,id为："+initStart);
            redisCacheUtil.save(idKey,String.valueOf(initStart));
            return String.valueOf(initStart);
        }
        // 不是第一次的情况
        Integer currentId = Integer.valueOf(String.valueOf(data));
        Integer netId = currentId+1;
        String netIdStr = String.valueOf(netId);
        redisCacheUtil.save(idKey,netIdStr);
        logger.info("获取当前id,id为："+netIdStr);
        return netIdStr;
    }
}
