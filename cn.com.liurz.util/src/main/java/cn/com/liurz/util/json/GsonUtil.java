package cn.com.liurz.util.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class GsonUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(GsonUtil.class);
    private static final Gson gson = new Gson();

    /**
     * 对象转为json字符串
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        if (object != null) {
            try {
                return gson.toJson(object);
            } catch (Exception e) {
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
                return gson.fromJson(json,classType);
            } catch (Exception e) {
                LOGGER.error("json转为对象异常",e);
            }
        }
        return null;
    }

    /**
     * 将json字符串转为list集合对象
     * @param json
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> classType) {
        if (StringUtils.isNotBlank(json) && classType != null) {
            try {
                return gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
            } catch (Exception e) {
                LOGGER.error("json转为list异常",e);
            }
        }
        return null;
    }
    /**
     * 将json字符串转为set集合对象
     * @param json
     * @param classType
     * @param <T>
     * @return
             */
    public static <T> Set<T> jsonToSet(String json, Class<T> classType) {
        if (StringUtils.isNotBlank(json) && classType != null) {
            try {
                return gson.fromJson(json, new TypeToken<Set<T>>() {}.getType());
            } catch (Exception e) {
                LOGGER.error("json转为list异常",e);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        TestVO testvo = new TestVO();
        testvo.setAge("age11");
        testvo.setName("testname");
        String jsonStr =GsonUtil.beanToJson(testvo);
        LOGGER.info(jsonStr);
    }
}
