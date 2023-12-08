package cn.com.liurz.flow.listener;

import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册监听类:针对全局监听器
 */
@Component
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private GlobalEventListener globalEventListener;

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        List<ActivitiEventListener> activitiEventListener = new ArrayList<>();
        activitiEventListener.add(globalEventListener);// 配置全局监听器
        processEngineConfiguration.setEventListeners(activitiEventListener);
    }
}