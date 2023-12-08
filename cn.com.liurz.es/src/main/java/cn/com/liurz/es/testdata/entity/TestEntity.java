package cn.com.liurz.es.testdata.entity;

import cn.com.liurz.es.vo.PrimaryKey;
import lombok.Data;

@Data
@PrimaryKey(value="id")
public class TestEntity {

    private String id;

    private String message;

    private int age;
}
