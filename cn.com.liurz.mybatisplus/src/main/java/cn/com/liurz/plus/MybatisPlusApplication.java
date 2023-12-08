package cn.com.liurz.plus;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * mybatisplus
 *
 * 如果有使用mybatis-plus，且安装了MyBatisX插件，比较直观的看到xxxMapper.java中的方法和xxxMapper.xml中的id对应情
 * 2023-08-17
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = {"cn.com.liurz.plus.mapper"})
@EnableEncryptableProperties
@EnableOpenApi  //开启 Swagger
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
