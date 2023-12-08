package cn.com.liurz.flow.config;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * 为了自定义数据表主键生效
 */
// @Configuration
public class ProcessEngineConfigurationConfig {

    @Autowired
    private DistributedIdGenerator distributedIdGenerator;
    @Bean
    public ProcessEngineConfigurationImpl processEngineConfigurationImpl(ProcessEngineConfigurationImpl processEngineConfigurationImpl){
        processEngineConfigurationImpl.setIdGenerator(distributedIdGenerator);
        processEngineConfigurationImpl.getDbSqlSessionFactory().setIdGenerator(distributedIdGenerator);
        return processEngineConfigurationImpl;
    }

}
