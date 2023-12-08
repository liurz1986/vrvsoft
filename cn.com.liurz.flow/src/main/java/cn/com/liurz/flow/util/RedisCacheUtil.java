package cn.com.liurz.flow.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * Redis操作工具类
 */
// @Component
public class RedisCacheUtil {



	@Autowired
	private RedisTemplate<String, String> assetTemplate;

	/**
	 * 多个hash保存
	 * @param key
	 * @param params
	 */
	public void saveBatchHash(String key , Map<String,String> params) {

		assetTemplate.opsForHash().putAll(key,params);
	}
	/**
	 * 单个hash保存
	 * @param key
	 * @param keyOne
	 * @param value
	 */
	public void saveHash(String key ,String keyOne, String value) {
		Boolean hasKey = assetTemplate.opsForHash().hasKey(key, keyOne);
		if(Boolean.TRUE.equals(hasKey)) {
			assetTemplate.opsForHash().delete(key, keyOne);
		}
		assetTemplate.opsForHash().put(key, keyOne, value);
	}

	/**
	 * 普遍直接保存key，value
	 * @param key
	 * @param value
	 */
	public void save(String key ,String value) {
		if(Boolean.TRUE.equals(assetTemplate.hasKey(key))) {
			assetTemplate.delete(key);
		}
		assetTemplate.opsForValue().set(key,value);
	}

	/**
	 * 普遍删除
	 * @param key
	 */
	public void delete(String key){
		assetTemplate.delete(key);
	}
	/**
	 * hash普遍删除
	 * @param key
	 */
	public void deleteHash(String key){
		if(Boolean.TRUE.equals(assetTemplate.hasKey(key))) {
			assetTemplate.delete(key);
		}
	}
	/**
	 * hash删除
	 * @param key
	 */
	public void deleteHash(String key, String keyOne){
		Boolean hasKey = assetTemplate.opsForHash().hasKey(key, keyOne);
		if(Boolean.TRUE.equals(hasKey)) {
			assetTemplate.opsForHash().delete(key,keyOne);
		}
	}


	public Object get(String key){
		return assetTemplate.opsForValue().get(key);
	}

	public Object getHash(String key,String keyone){
		return assetTemplate.opsForHash().get(key,keyone);
	}
	public Object getBatchHash(String key, List<Object> keyones){
		return assetTemplate.opsForHash().multiGet(key,keyones);
	}

	public Long sizeHash(String key){
		return assetTemplate.opsForHash().size(key);
	}

	public Long size(String key){
		return assetTemplate.opsForValue().size(key);
	}
}
