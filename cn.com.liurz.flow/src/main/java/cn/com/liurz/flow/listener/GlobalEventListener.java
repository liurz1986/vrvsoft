package cn.com.liurz.flow.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 全局监听器
 *
 * 不需要在acitivit中配置任务监听，需要代码中注册全局监听器
 */

@Component
public class GlobalEventListener implements ActivitiEventListener {

	private Logger logger = LoggerFactory.getLogger(GlobalEventListener.class);
	@Override
	public void onEvent(ActivitiEvent event) {
		logger.info("全局监听器:"+event.getType());
		switch (event.getType()) {
			case ENTITY_CREATED:
				logger.info("流程创建");
				break;
			case PROCESS_STARTED:
				logger.info("流程开始");
				break;

			case TASK_CREATED:
				logger.info("任务开始");
				break;
			case TASK_COMPLETED:
				logger.info("任务完成");
				break;
			case PROCESS_COMPLETED:
				logger.info("流程结束"); // 流程结束
				break;
			case PROCESS_CANCELLED:
				logger.info("流程取消");  // 流程结束
				break;
			case PROCESS_COMPLETED_WITH_ERROR_END_EVENT:
				logger.info("流程失败结束");// 流程结束
			default:
				break;
		}
	}

	/**
	 * 监听异常处理策略
	 * @return
	 */
	@Override
	public boolean isFailOnException() {

		return false;
	}

}
