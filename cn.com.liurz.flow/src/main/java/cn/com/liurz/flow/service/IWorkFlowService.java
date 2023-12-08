package cn.com.liurz.flow.service;

import cn.com.liurz.flow.vo.ResponseVo;
import cn.com.liurz.flow.vo.WorkFlowVo;

import java.util.List;
import java.util.Map;


public interface IWorkFlowService {
	/**
	 * 启动流程
	 *
	 * @param workFlowVo
	 * @return Map<String, Object>
	 */
	ResponseVo startProcess(WorkFlowVo workFlowVo) throws Exception;

	/**
	 * 审批任务：根据任务id进行处理
	 *
	 * @param workFlowVo
	 * 
	 */
	public ResponseVo complete(WorkFlowVo workFlowVo);

	/**
	 * 任务认领
	 * 
	 * @Title: setAssignee
	 * @Description: TODO
	 * @param taskId
	 * @param userId
	 * @return void
	 */
	void setAssignee(String taskId, String userId);

	/**
	 * 认领任务:setAssignee和claim两个的区别是在认领任务时，claim会检查该任务是否已经被认领，
	 * 如果被认领则会抛出ActivitiTaskAlreadyClaimedException
	 * ,而setAssignee不会进行这样的检查，其他方面两个方法效果一致。
	 * setOwner是在代理任务时使用，代表着任务的归属者，而这时，setAssignee代表的是代理办理者，
	 * 
	 * @param taskId
	 *            任务id
	 * @param userId
	 *            认领人id
	 */
	void claim(String taskId, String userId);

	/**
	 * 设置任务认领组
	 *
	 * @param taskId
	 * @param groupId
	 */
	void addCandidateGroup(String taskId, String groupId);

	/**
	 * 获取当前流程的处理人
	 * 
	 * @Title: getAssignee
	 * @Description: TODO
	 * @param processId
	 * @return
	 * @return String
	 */
	public Map<String, Object> getCurrentRunTask(String processId);

	/**
	 * 获取当前人待处理的任务
	 * 
	 * @Title: getTasksByAssignee
	 * @Description: TODO
	 * @param assignee
	 * @return
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getTasksByAssignee(String assignee);

	/**
	 * 通过任务id，查询任务信息
	 *
	 * @param taskId
	 * @return
	 */
	Map<String, Object> queryTaskById(String taskId);

	/**
	 * 删除流程
	 *
	 * @param processInstanceId
	 * @param deleteReason
	 * @return
	 * @throws Exception
	 */
	void deleteProcessInstance(String processInstanceId, String deleteReason) throws Exception;

	// 给当前任务参数赋值
	public void setTaskVariables(String taskId,Map<String,Object> taskparams);

	// 给流程增加参数
	public void setProcessVariables(String processId,Map<String,Object> taskparams);
	/**
	 * 根据任务Id获得对应的自定义表单内容
	 * @param taskId
	 * @return
	 */
	public String getTaskFormKey(String taskId);

}
