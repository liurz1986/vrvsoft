package cn.com.liurz.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacksonUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);
    private static final ObjectMapper mObjectMapper = new ObjectMapper();


    //在这里进行配置全局
    static {
        //有时JSON字符串中含有我们并不需要的字段，那么当对应的实体类中不含有该字段时，会抛出一个异常，告诉你有些字段（java 原始类型）没有在实体类中找到
        //设置为false即不抛出异常，并设置默认值 int->0 double->0.0
        mObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转为json字符串
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        if (object != null) {
            try {
                return mObjectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                LOGGER.error("对象转为json字符串异常",e);
            }
        }
        return "";
    }

    /**
     * 将json字符串转为对象
     * @param json
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> classType) {
        if (StringUtils.isNotBlank(json) && classType != null) {
            try {
                return mObjectMapper.readValue(json, classType);
            } catch (IOException e) {
                LOGGER.error("json转为对象异常",e);
            }
        }
        return null;
    }

    /**
     * 将json字符串转为集合对象
     * @param json
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> classType) {
        if (StringUtils.isNotBlank(json) && classType != null) {
            try {
                return mObjectMapper.readValue(json, mObjectMapper.getTypeFactory().constructCollectionType(List.class, classType));
            } catch (IOException e) {
                LOGGER.error("json转为list异常",e);
            }
        }
        return null;
    }

    public static <k, v> Map<k, v> jsonToMap(String json, Class<k> kType, Class<v> vType) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return mObjectMapper.readValue(json, mObjectMapper.getTypeFactory().constructMapType(Map.class, kType, vType));
            } catch (IOException e) {
                LOGGER.error("json转为map异常",e);
            }
        }
        return null;
    }

}
