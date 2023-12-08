package cn.com.liurz.flow.listener;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 流程监听器(执行监听器)
 *
 * 作用于：开始节点、结束节点、连线上、任务节点上
 * start ： 节点开始
 * end ： 节点结束
 * take ：作用于连线上
 *
 */
@Service("customerFlowEventListener")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerFlowEventListener implements ExecutionListener {
	private Logger logger = LoggerFactory.getLogger(CustomerFlowEventListener.class);

	@Autowired
	private RuntimeService runtimeService;
	@Override
	public void notify(DelegateExecution execution)  {
		String processInstanceId = execution.getProcessInstanceId(); // 流程实例id
		//设置流程变量，流程没有结束前都可以拿到
		runtimeService.setVariable(processInstanceId,"status","pass");
		String executionId = execution.getId();
		String eventName = execution.getEventName();
		String processDefinitionId = execution.getProcessDefinitionId(); // act_re_procdef表中Id_的值
		//设置流程变量，流程没有结束前都可以拿到
		runtimeService.setVariable(processInstanceId,"status","pass");
		logger.info("执行监听器，eventName :"+eventName);
		if(StringUtils.isNotEmpty(eventName)) {
			switch (eventName) {
				case "start":
					logger.info("流程监听器==节点开始");
					break;
				case "end":
					logger.info("流程监听器==节点结束");
					break;
				case "take":  // 在流程连线上使用的监听器
					logger.info("流程监听器===为连线时监听");
					break;
				default:
					break;

			}
		}
		}
}
