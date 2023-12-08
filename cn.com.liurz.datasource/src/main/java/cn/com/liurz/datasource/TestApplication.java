package cn.com.liurz.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 多数据源配置
 */
@SpringBootApplication
public class TestApplication {
    public static void  main(String[] args){
        SpringApplication.run(TestApplication.class, args);

    }
}
