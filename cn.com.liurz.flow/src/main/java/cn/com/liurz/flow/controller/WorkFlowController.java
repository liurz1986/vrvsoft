package cn.com.liurz.flow.controller;

import cn.com.liurz.flow.service.IWorkFlowService;
import cn.com.liurz.flow.util.Constants;
import cn.com.liurz.flow.vo.ResponseVo;
import cn.com.liurz.flow.vo.WorkFlowVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程处理
 * 
 * @ClassName: WorkFlowController
 * @Description: TODO
 * @author lwx393577：
 * @date 2020年4月26日 下午9:49:33
 *
 */
@RestController
@RequestMapping("workflow")
public class WorkFlowController {
	private Logger logger = LoggerFactory.getLogger(WorkFlowController.class);

	@Autowired
	private IWorkFlowService workFlowService;

	/**
	 * 开启流程
	 * 
	 * @Title: startProcess
	 * @Description: TODO
	 * @return
	 * @throws Exception
	 * @return Map<String,Object>
	 */
	// http://localhost:8088/activitServer/workflow/start
	// 参数：{"processDefinitionKey":"processtest001","workFlowParams":{"userList":["张三","李四","王五"]}}
	// 返回值：{"status": "success","message": null,"errorDetail": null, "items":
	// {"processId": "22508", "taskId": "22513" }}
	// {"processDefinitionKey":"process"}
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public ResponseVo startProcess(@RequestBody WorkFlowVo workFlowVo) throws Exception {
		ResponseVo result = new ResponseVo();
		try {
			return workFlowService.startProcess(workFlowVo);
		} catch (Exception e) {
			logger.error("开启流程失败：" + e);
			result.setErrorDetail(e.getMessage());
			result.setMessage("开启流程失败");
			result.setStatus(Constants.FAIL);
			return result;
		}
	}

	/**
	 * 任务审批：包括设置下一个节点处理人
	 *        通过任务id或任务key进行流程审批
	 * 
	 * @Title: complete
	 * @Description: TODO
	 * @param workFlowVo workFlowVo
	 * @throws Exception
	 * @return Map<String,Object>
	 */
	// http://localhost:8088/activitServer/workflow/complete
	// 参数：{"taskId":"22516","workFlowParams":{"approve":"liurz"}}
	 // {"taskDefKey":"_9","workFlowParams":{"approve":"liurz"}}  {"processDefinitionKey":"signerTestDemo","workFlowParams":{"userLists":[32,35,56]}}
	// 返回值：{"status":"success","message":null,"errorDetail":null,"items":{"processId":"22508","taskId":"22516"}}
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public ResponseVo complete(@RequestBody WorkFlowVo workFlowVo) throws Exception {
		ResponseVo result = new ResponseVo();
		try {
			return workFlowService.complete(workFlowVo);
		} catch (Exception e) {
			logger.error("任务审批失败：" + e);
			result.setErrorDetail(e.getMessage());
			result.setMessage("任务审批失败");
			result.setStatus(Constants.FAIL);
			return result;
		}

	}

	/**
	 * 任务认领（就是设置当前任务的处理人）
	 * 
	 * @Title: claim
	 * @param workFlowVo
	 * @return
	 * @throws Exception
	 * @return Map<String,Object>
	 */
	// http://localhost:8088/activitServer/workflow/claim {"taskId":"57545","claimant":"testCla"}
	@RequestMapping(value = "/claim", method = RequestMethod.POST)
	public ResponseVo claim(@RequestBody WorkFlowVo workFlowVo) throws Exception {
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getTaskId()) || StringUtils.isEmpty(workFlowVo.getClaimant())) {
				logger.info("taskId和认领人不能为空");
				result.setMessage("taskId或认领人不能为空");
				result.setStatus(Constants.FAIL);
				return result;
			}
			logger.info("任务认领开始,taskId:" + workFlowVo.getTaskId() + "--claim user:" + workFlowVo.getClaimant());
			workFlowService.claim(workFlowVo.getTaskId(), workFlowVo.getClaimant());
			result.setMessage("任务认领成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("任务认领成功");
			return result;
		} catch (Exception e) {
			result.setMessage("任务认领失败");
			result.setErrorDetail(e.getMessage());
			result.setStatus(Constants.FAIL);
			logger.error("任务认领失败", e);
			return result;
		}
	}

	/**
	 * 任务转移(所谓的转单)：就是重新设任务处理人
	 * act_ru_task中ASSIGNEE_的值改成现有的处理人
	 * @Title: transferTask
	 * @Description: TODO
	 * @param workFlowVo
	 * @return void
	 */
	// http://localhost:8088/activitServer/workflow/transferTask {"taskId":"57545","assginne":"testCla"}
	@RequestMapping(value = "/transferTask", method = RequestMethod.POST)
	public ResponseVo transferTask(@RequestBody WorkFlowVo workFlowVo) {
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getTaskId()) || StringUtils.isEmpty(workFlowVo.getAssginne())) {
				result.setMessage("taskId或转移人不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("taskId或转移人不能为空");
				return result;
			}
			logger.info("任务转移开始,taskId:" + workFlowVo.getTaskId() + "--transfer user:" + workFlowVo.getAssginne());
			workFlowService.setAssignee(workFlowVo.getTaskId(), workFlowVo.getAssginne());
			result.setMessage("任务转移成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("任务认领成功");
		} catch (Exception e) {
			result.setMessage("任务转移失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("任务转移失败", e);
		}
		return result;
	}

	/**
	 * 获取流程当前节点的任务信息
	 * 
	 * @Title: getAssignee
	 * @Description: TODO
	 * @param processId
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/getCurrentRunTask?processId=5001
	@RequestMapping(value = "/getCurrentRunTask", method = RequestMethod.GET)
	public ResponseVo getCurrentRunTask(@RequestParam("processId") String processId) {
		logger.info("---proccessId--", processId);
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(processId)) {
				result.setMessage("流程id不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("t流程id不能为空");
				return result;
			}
			Map<String, Object> items = workFlowService.getCurrentRunTask(processId);
			result.setMessage("获取流程的当前任务信息");
			result.setStatus(Constants.SUCCESS);
			result.setItems(items);
			logger.info("获取流程的当前任务信息成功");
		} catch (Exception e) {
			result.setMessage("获取流程的当前任务信息失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("获取流程的当前任务信息失败", e);
		}
		return result;
	}

	/**
	 * 代办任务--当前处理人的所有代办任务信息
	 * 
	 * @Title: getTasksByAssignee
	 * @Description: TODO
	 * @param assignee
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/getTasksByAssignee?assignee=liurz
	@RequestMapping(value = "/getTasksByAssignee", method = RequestMethod.GET)
	public ResponseVo getTasksByAssignee(@RequestParam("assignee") String assignee) {
		logger.info("===getTasksByAssignee start,assignee {} ", assignee);
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(assignee)) {
				result.setMessage("当前处理人不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("当前处理人不能为空");
				return result;
			}
			List<Map<String, String>> taskes = workFlowService.getTasksByAssignee(assignee);
			result.setMessage("代办任务获取成功");
			result.setStatus(Constants.SUCCESS);
			result.setItems(taskes);
			logger.info("代办任务获取成功");
		} catch (Exception e) {
			result.setMessage("代办任务获取失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("代办任务获取失败", e);
		}
		return result;
	}

	/**
	 * 获取任务信息
	 * 
	 * @Title: queryTaskById
	 * @Description: TODO
	 * @param taskId
	 * @return
	 * @return ResponseVo
	 */
	@RequestMapping(value = "/queryTaskById", method = RequestMethod.GET)
	public ResponseVo queryTaskById(@RequestParam("taskId") String taskId) {
		logger.info("===queryTaskById start,taskId {} ", taskId);
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(taskId)) {
				result.setMessage("taskId不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("taskId不能为空");
				return result;
			}
			Map<String, Object> taske = workFlowService.queryTaskById(taskId);
			result.setMessage("获取任务信息成功");
			result.setStatus(Constants.SUCCESS);
			result.setItems(taske);
			logger.info("获取任务信息成功");
		} catch (Exception e) {
			result.setMessage("获取任务信息失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("获取任务信息失败", e);
		}
		return result;
	}

	/**
	 * 删除流程（针对没有结束的流程）
	 * 
	 * @Title: deleteProcess
	 * @Description: TODO
	 * @param workFlowVo
	 * @return
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/deleteProcess
	// {"processId":"5001","remark":"删除原因"}
	@RequestMapping(value = "/deleteProcess", method = RequestMethod.POST)
	public ResponseVo deleteProcess(@RequestBody WorkFlowVo workFlowVo) {
		logger.info("===deleteProcess,processId:{} ", workFlowVo.getProcessId());
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getProcessId())) {
				result.setMessage("流程Id不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("流程Id不能为空");
				return result;
			}
			workFlowService.deleteProcessInstance(workFlowVo.getProcessId(), workFlowVo.getRemark());
			result.setMessage("删除流程成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("删除流程成功");
		} catch (Exception e) {
			result.setMessage("删除流程失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("删除流程失败", e);
		}
		return result;
	}

	// 给当前任务参数赋值:
	// http://localhost:8080/activitServer/workflow/setTaskVariables
	// {"taskId":"57545","remark":"删除原因"}
	@RequestMapping(value = "/setTaskVariables", method = RequestMethod.POST)
	public ResponseVo setTaskVariables(@RequestBody WorkFlowVo workFlowVo){
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getTaskId())) {
				result.setMessage("流程Id不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("流程Id不能为空");
				return result;
			}
			workFlowService.setTaskVariables(workFlowVo.getTaskId(),workFlowVo.getWorkFlowParams());
			result.setMessage("给当前任务参数赋值成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("给当前任务参数赋值成功");
		} catch (Exception e) {
			result.setMessage("给当前任务参数赋值失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("给当前任务参数赋值失败", e);
		}
		return result;
	}

	// 给流程增加参数 act_ru_variable表新增参数
	// http://localhost:8080/activitServer/workflow/setProcessVariables
	// {"processId":"57534","workFlowParams":{"remark":"删除原因"}}
	@RequestMapping(value = "/setProcessVariables", method = RequestMethod.POST)
	public ResponseVo setProcessVariables(@RequestBody WorkFlowVo workFlowVo){
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getProcessId())) {
				result.setMessage("流程Id不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("流程Id不能为空");
				return result;
			}
			workFlowService.setProcessVariables(workFlowVo.getProcessId(),workFlowVo.getWorkFlowParams());
			result.setMessage("给流程增加参数成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("给流程增加参数成功");
		} catch (Exception e) {
			result.setMessage("给流程增加参数失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("给流程增加参数失败", e);
		}
		return result;
	}
	
	@RequestMapping(value = "/external/api/assets/group/win", method = RequestMethod.GET)
	public List<Map<String,Object>> orgs(){
		List<Map<String,Object>> datas = new ArrayList<>();
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("id", 542);
		result.put("parent", 541);
		result.put("name", "海上平台物料配备管理系统");
		result.put("relationshipName", "海油发展/配餐公司/海上平台物料配备管理系统");
		result.put("owner", 1);
		result.put("platform", 1);
		result.put("description","");
		datas.add(result);
		return datas;
	}

	/**
	 * 获取任务的自定义表单
	 * @param taskId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    // http://localhost:8088/activitServer/getTaskFormKey/1327
	@GetMapping("/getTaskFormKey/{taskId}")
	public String getTaskFormKey(@PathVariable String taskId) throws UnsupportedEncodingException {
		String formKey = workFlowService.getTaskFormKey(taskId);
		formKey = URLDecoder.decode(formKey,"utf-8");
		return formKey;
	}

}
