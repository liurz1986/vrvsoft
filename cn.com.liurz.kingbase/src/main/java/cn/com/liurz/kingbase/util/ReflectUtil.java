package cn.com.liurz.kingbase.util;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReflectUtil {

    /**
     * map转成对象
     *
     * map中的可以与对象的字段名一致
     * @param maps
     * @param cls
     * @param <T>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public  static <T> T mapToObj(Map<String,Object> maps,Class<T> cls) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Set<String> keys =  maps.keySet();
        T newObj = cls.newInstance();
        for(String key : keys){
            Field field =  cls.getDeclaredField(key);
            if(null == field){
                continue;
            }
            field.setAccessible(true);
            field.set(newObj,maps.get(key));
        }
      return newObj;
    }


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Map<String,Object> maps = new HashMap<>();
        maps.put("id","test");
        maps.put("name","testName");
        Test newObj = mapToObj(maps,Test.class);
        System.out.println(newObj.getName());
    }

    public void baseTest() throws  ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException  {
        //通过全类名加载字节码对象
        Class classes =Class.forName("cn.com.liurz.kingbase.util.Test");
        // 获取构造函数
        Constructor constructor = classes.getConstructor(String.class,String.class);
        // 通过构造方法，构造对象
        Object testObj = constructor.newInstance("1","name");

        // 通过属性名获取属性
        Field field = classes.getDeclaredField("id");
        field.setAccessible(true);
        // 获取obj对象中字段为id的值
        String id = (String)field.get(testObj);
        System.out.println("获取Test对象中id的值："+id);
        // 通过方法名和参数类型获取方法
        Method method = classes.getMethod("getById",String.class);
        // obj对象调用改方法,并传入参数
        Object result = method.invoke(testObj,"xxtest");
        System.out.println("Test对象调用getById方法传入参数获取结果："+result);

        // 获取obj所有方法
        Method[] methods =classes.getMethods();
        for(Method method1 : methods){
            // 获取有Bean注解的
            Annotation an = method1.getAnnotation(Bean.class);
            if(null != an){
                System.out.println("存在bean注解的方法");
            }
        }
    }
}
