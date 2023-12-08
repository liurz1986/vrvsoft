package cn.com.liurz.flow.listener;

import cn.com.liurz.flow.vo.FlowConstant;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;




/**
 * 主要用于连线上，用于设置流程失败结束
 *
 */
@Service("instanceResultFailedListener")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InstanceResultFailedListener implements ExecutionListener {
	private Logger logger = LoggerFactory.getLogger(CustomerFlowEventListener.class);
	@Override
	public void notify(DelegateExecution execution)  {
		logger.info("连线上流程失败结束监听器");
		execution.setVariable(FlowConstant.PROCESSRESULT, false);
	}

}
