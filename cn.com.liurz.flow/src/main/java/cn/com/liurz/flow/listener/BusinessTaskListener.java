package cn.com.liurz.flow.listener;


import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.FixedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 任务界节点监听器
 *
 * 作用于：任务节点上
 *
 */

@Service("businessTaskListener")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public  class BusinessTaskListener implements TaskListener {
	private Logger logger = LoggerFactory.getLogger(BusinessTaskListener.class);
	private FixedValue candidateType;
	private FixedValue candidate; // TODO 根据逗号分隔。进一步可以考虑使用流程变量占位符。
    @Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Override
	public void notify(DelegateTask delegateTask) {
		// 根据province的组织机构节点获取对应的人，来创建任务和任务候选人
		String eventName = delegateTask.getEventName();
		String taskId = delegateTask.getId();
		String processInstanceId = delegateTask.getProcessInstanceId();
		String status1 = (String)runtimeService.getVariable(processInstanceId,"status");
		String status2 = (String)taskService.getVariable(taskId,"status");
		logger.info("任务监听器："+eventName+":"+delegateTask+":"+delegateTask.getProcessInstanceId());
		switch (eventName) {
		case "create":
			this.createBusinessTask(delegateTask);
			break;
		case "delete":
			// 删除任务和对应的候选人实体对象
			this.deleteTask(taskId);
			logger.info("删除任务"+delegateTask.getName()+"成功");
			break;
		default:
			break;
		}
	}
    
	/**
	 * 创建一个BusinessTask任务
	 * @param delegateTask
	 */
	
	private void createBusinessTask(DelegateTask delegateTask) {

		logger.info("新增任务"+delegateTask.getName()+"成功");
	}


	/**
	 * 完成任务，转移到下一任务
	 * @param taskId
	 */
	private void deleteTask(String taskId){

	}




}
