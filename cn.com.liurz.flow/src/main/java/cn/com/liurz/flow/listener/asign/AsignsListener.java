package cn.com.liurz.flow.listener.asign;


import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 人员会签监听器配置
 * 1. 全部审批通过
 * 2.
 */
@Service("asignsListener")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AsignsListener implements TaskListener {
    private Logger logger = LoggerFactory.getLogger(AsignsListener.class);
    @Override
    public void notify(DelegateTask delegateTask) {
        String signer = delegateTask.getVariable("signer").toString(); // 设计器自己定义的
        logger.info("signer对应的值："+signer);
        Set<String> users = new HashSet<>();
        users.add(signer);
    }
}
