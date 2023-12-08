package cn.com.liurz.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    public void put(String key,String data){
        if(redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
        }
        redisTemplate.opsForValue().set(key,data);
    }

    /**
     * TimeUnit.DAYS //天
     * TimeUnit.HOURS //小时
     * TimeUnit.MINUTES //分钟
     * TimeUnit.SECONDS //秒
     * TimeUnit.MILLISECONDS //毫秒
     * @param key
     * @param data
     * @param expireTime
     */
    public void put(String key,String data, long  expireTime,TimeUnit timeUnit){
        if(redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
        }
        redisTemplate.opsForValue().set(key,data,expireTime, timeUnit);
    }

    public Object get(String key){
        return  redisTemplate.opsForValue().get(key);
    }

    public void hashPut(String key,String mapKey,String mapValue){
        if(redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
        }
        redisTemplate.opsForHash().put(key,mapKey,mapValue);
    }

    public Object hashGet(String key,String mapKey){
        return  redisTemplate.opsForHash().get(key,mapKey);
    }

    /**
     * 以map集合的形式添加键值对
     * @param key
     * @param maps
     */
    public void hashPutAll(String key, Map<String,Object> maps){
        redisTemplate.opsForHash().putAll(key,maps);

    }

    /**
     * 查看是否存在
     * @param key
     * @return
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 查看hash表中指定字段是否存在
     * @param key
     * @param field
     * @return
     */
    public boolean hashExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 批量获取值
     * @param keys
     * @return
     */
    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 删除单个key值
     * @param key
     */
    public void delete(String key){
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key值
     * @param keys
     */
    public void batchDelete(Collection<String> keys){
        redisTemplate.delete(keys);
    }
}
