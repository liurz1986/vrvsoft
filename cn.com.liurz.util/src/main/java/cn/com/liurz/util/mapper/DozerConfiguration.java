package cn.com.liurz.util.mapper;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


/**
 * dozer配置
 *
 * 主要是配置实体与实体转换读取的xml文件；实体间属性名称一样不用配置xml进行具体转换
 */
@Configuration
public class DozerConfiguration {
    public DozerConfiguration() {
    }

    @Bean(name = {"org.dozer.Mapper"})
    public DozerBeanMapperFactoryBean dozerBean(@Value("classpath*:mappings/mappings-*.xml") Resource[] resources) {
        DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.setMappingFiles(resources);
        return dozerBeanMapperFactoryBean;
    }
}