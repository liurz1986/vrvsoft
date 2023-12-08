package cn.com.liurz.flow.controller;

import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/activitPage")
public class ActivitPageController {

	
	private static Logger logger = LoggerFactory.getLogger(ActivitPageController.class);
	private static final String MULTIPART="multipart/form-data;charset=utf8";
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;

	@GetMapping("/processPage")
	public String processPage(String processId, Model model) {
		model.addAttribute("processId", processId);
		return "flowshow";
	}

	/**
	 * 获得流程图 1.子流程 2.结束节点 3.一般节点
	 *
	 * 获取进度图异常   :生成不了图片
	 */
	@RequestMapping("/processDiagram")
	@ApiOperation(value="查询流程进度图",notes="查询流程进度图")
	public void processDiagram(String processId, HttpServletResponse response) {

		showDiagram(processId, response);
	}
	
	@GetMapping("/definePage")
	public String definePage(String deployId, Model model) {
		model.addAttribute("deployId", deployId);
		return "defineshow";
	}

	@RequestMapping("/processDefine")  // 7版本生成不了图片，采用5的版本可以生成图片
	public void getDiagramEnd(String processDefinitionId,HttpServletResponse response) throws IOException {
		// 获取BPMN模型对象
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
		// BPMN模型对象、图片类型、显示的节点
		String fontName = "宋体";
		List<String> currentActs = new ArrayList<>();
		ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
		InputStream is =processDiagramGenerator.generateDiagram(
				model, "png", currentActs, new ArrayList<String>(), fontName, fontName, fontName, null, 1.0);
		response.setContentType(MULTIPART);
		OutputStream out = null;
		out = response.getOutputStream();
		out.write(getImgByte(is));
		out.flush();
		out.close();
	}
	@RequestMapping("/processDefineByDeploy") // 工单预览-工单流程 7版本生成不了图片，采用5的版本可以生成图片
	public void processDefineByDeploy(String deployId,HttpServletResponse response) throws IOException {
		// 获取BPMN模型对象
		ProcessDefinition singleResult = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
		BpmnModel model = repositoryService.getBpmnModel(singleResult.getId());
		// BPMN模型对象、图片类型、显示的节点
		String fontName = "宋体";
		List<String> currentActs = new ArrayList<>();
		// bpmn文件中的元素信息：节点及连线
		List<Process> processes = model.getProcesses();
		Collection<FlowElement> flowElementList = processes.get(0).getFlowElements();
		// 结束节点进行高亮显示
		for(FlowElement flowElement : flowElementList){
			String name = flowElement.getName();
			// 节点名称为开始信息，开始节点名称规定为开始，结束节点名称固定为结束，其他节点名称为界面上配置填写的名称
			if("结束".equals(name)){
				EndEvent startEvent = (EndEvent)flowElement;
				currentActs.add(startEvent.getId());
			}
		}
		// 使用DefaultProcessDiagramGenerator生成流程图输入流
		ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
		InputStream is =processDiagramGenerator.generateDiagram(model, "png", currentActs, new ArrayList<String>(), fontName, fontName, fontName, null, 1.0);
		response.setContentType(MULTIPART);
		OutputStream out = null;
		out = response.getOutputStream();
		out.write(getImgByte(is));
		out.flush();
		out.close();
	}

	// 将输入流转换为byte数组
	private byte[] getImgByte(InputStream is) throws IOException {
		BufferedInputStream bufin = new BufferedInputStream(is);
		int buffSize = 1024;
		ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);

		byte[] temp = new byte[buffSize];
		int size = 0;
		while ((size = bufin.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		bufin.close();
		is.close();
		byte[] content = out.toByteArray();
		out.close();
		return content;
	}
	
	/**
	 * 流程执行中
	 * @param processInstanceId
	 * @return
	 */
	public InputStream getDiagram(String processInstanceId) {
		// 查询流程实例
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// 查询流程实例
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(pi.getProcessDefinitionId()).singleResult();
		// 获取BPMN模型对象
		BpmnModel model = repositoryService.getBpmnModel(pd.getId());
		// 定义使用宋体
		String fontName = "宋体";
		// 获取流程实实例当前点的节点，需要高亮显示
		List<String> currentActs = runtimeService.getActiveActivityIds(pi.getId());
		ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
		// BPMN模型对象、图片类型、显示的节点
		InputStream is = processDiagramGenerator.generateDiagram(model, "png", currentActs, new ArrayList<String>(),
						fontName, fontName, fontName,null, 1.0);
		return is;
	}

	public String showDiagram(String processId, HttpServletResponse response) {
		OutputStream out = null;
		try {
			InputStream is = getDiagram(processId);
			response.setContentType(MULTIPART);
			out = response.getOutputStream();
			out.write(getImgByte(is));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out!=null){
					out.close();					
				}
			} catch (Exception e) {
				logger.error("输出图像失败：{}", e);
			}
		}
		return null;
	}
}
