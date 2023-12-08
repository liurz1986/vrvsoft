package cn.com.liurz.util.Serializable;

import lombok.Data;

import java.io.Serializable;

/**
 * 系列化：
 * 1.实现Serializable接口
 * 2. transient 修饰的属性，不会被序列化的
 * 3. 静态static的属性，不会被序列化
 */
@Data
public class FlyPig implements Serializable {
    // 系列化版本标识 : 可以确保代码一致时反序列化成功,一般就写为1L
    // 如果不设置serialVersionUID的话，Java会自动给赋值会出现反向系列化失败
    private static final long serialVersionUID = 1L;

    private static String AGE = "269E"; // 静态static的属性，他不序列化
    private String name;
    private String color;
    transient private String car;   //transient 修饰的属性，是不会被序列化的
    @Override
    public String toString() {
        return "FlyPig{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", car='" + car + '\'' +
                ", AGE='" + AGE + '\'' +
                //", addTip='" + addTip + '\'' +
                '}';
    }
}
