package cn.com.liurz.flow.vo;

import java.util.Map;

/**
 * 流程处理对象
 * 
 * @ClassName: WorkFlowVo
 * @Description: TODO
 * @author lwx393577：
 * @date 2020年4月26日 下午10:00:56
 *
 */
public class WorkFlowVo {
	/**
	 * 流程处理参数：节点处理人、分支的判断条件参数、增加的流程备注信息
	 */
	private Map<String, Object> workFlowParams;
	/**
	 * 当前任务id
	 */
	private String taskId;
	/**
	 * 流程图中定义节点Name的值
	 */
	private String taskName;
	/**
	 * 流程图中定义节点Id的值
	 */
	private String taskDefKey;

	/**
	 * 流程的文件名称，开启流程使用的:流程id存在采用id，id不存在采用名称
	 */
	private String processDefinitionKey;

	/**
	 * 流程Id
	 */
	private String processId;
	/**
	 * 流程处理人
	 */
	private String assginne;
	/**
	 * 任务认领人
	 */
	private String claimant;
	/**
	 * 备注信息：比如删除时的理由
	 */
	private String remark;

	public Map<String, Object> getWorkFlowParams() {
		return workFlowParams;
	}

	public void setWorkFlowParams(Map<String, Object> workFlowParams) {
		this.workFlowParams = workFlowParams;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getAssginne() {
		return assginne;
	}

	public void setAssginne(String assginne) {
		this.assginne = assginne;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getClaimant() {
		return claimant;
	}

	public void setClaimant(String claimant) {
		this.claimant = claimant;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
