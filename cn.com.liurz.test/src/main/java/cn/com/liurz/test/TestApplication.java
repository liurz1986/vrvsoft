package cn.com.liurz.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.com.liurz.util", "cn.com.liurz.test"})
public class TestApplication {
    public static void  main(String[] args){
        SpringApplication.run(TestApplication.class, args);
    }
}
