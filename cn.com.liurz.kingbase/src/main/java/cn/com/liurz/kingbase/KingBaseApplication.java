package cn.com.liurz.kingbase;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *
 * 人大金仓数据库+mybatisplus
 *
 *
 * 2023-11-13
 */
@SpringBootApplication
@MapperScan(basePackages = {"cn.com.liurz.kingbase.mapper"})
@EnableEncryptableProperties
public class KingBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(KingBaseApplication.class, args);
    }

}
