package cn.com.liurz.flow.controller.resource;

import cn.com.liurz.flow.util.PathUtil;
import cn.com.liurz.flow.util.ResponseResult;
import cn.com.liurz.flow.util.UUIDUtils;
import cn.com.liurz.flow.vo.DeployInfoVO;
import cn.com.liurz.flow.vo.DeploymentResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 *  工作流操作类
 * @version V1.0
 * @Package org.com.liurz.iresources.activiti.controller
 * @date 2021/9/5 16:56
 * @Copyright © 2020-2028
 */
@Controller
public class ModelController {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);
    @Autowired
    //流程服务组件：用于流程定义和存取
    private RepositoryService repositoryService;
    @Autowired
    //历史服务组件：用于获取正在运行或已经完成的流程实例的信息
    private HistoryService historyService;
    @Autowired
    //运行时服务组件：提供了启动流程、查询流程实例、设置获取流程实例变量等功能。
    private RuntimeService runtimeService;

    //数据模型转换
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    ProcessEngineFactoryBean processEngine;

    @Autowired
    private TaskService taskService;

    private static final String MULTIPART="multipart/form-data;charset=utf8";

    /**
     * 首页控制器：获取工作流模型列表控制器
     *
     * @param modelAndView 页面对象
     * @return 返回页面对象
     */
    @RequestMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("views/index");
        //通过流程服务组件获取当前的工作流模型列表
        List<Model> actList = repositoryService.createModelQuery().list();
        modelAndView.addObject("actList", actList);
        return modelAndView;
    }

    /**
     * 跳转编辑器/编辑工作流页面
     *
     * @return
     */
    @GetMapping("/editor")
    public String editor() {
        return "views/modeler";
    }

    /**
     * 创建模型
     *
     * @param response
     */
    @RequestMapping("/create")
    public void create(HttpServletResponse response) throws IOException {
        //创建一个空模型
        Model model = repositoryService.newModel();

        //设置一下默认信息
        String modelName = "new-model";//模型名称
        String modelKey = "new-key";// 模型key
        String modelDescription = ""; //模型描述
        int modelVersion = 1; //默认版本号


        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, modelDescription);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, modelVersion);
        model.setName(modelName);
        model.setKey(modelKey);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        createObjectNode(model.getId());
        response.sendRedirect("/activitServer/editor?modelId=" + model.getId());
        logger.info("创建模型结束，返回模型ID：{}", model.getId());
    }

    /**
     * 创建模型时完善ModelEditorSource
     *
     * @param modelId
     */
    @SuppressWarnings("/deprecation")
    private void createObjectNode(String modelId) {
        logger.info("创建模型完善ModelEditorSource入参模型ID：{}", modelId);
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes("utf-8"));
        } catch (Exception e) {
            logger.info("创建模型时完善ModelEditorSource服务异常：{}", e);
        }
        logger.info("创建模型完善ModelEditorSource结束");
    }

    /**
     * 发布流程
     *
     * @param modelId 模型ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/publish")
    public Object publish(String modelId) {
        logger.info("流程部署入参modelId：{}", modelId);
        Map<String, String> map = new HashMap<String, String>();
        try {
            Model modelData = repositoryService.getModel(modelId);
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
            if (bytes == null) {
                logger.info("部署ID:{}的模型数据为空，请先设计流程并成功保存，再进行发布", modelId);
                map.put("code", "FAILURE");
                return map;
            }
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addBpmnModel(modelData.getKey() + ".bpmn20.xml", model)
                    .deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
            map.put("code", "SUCCESS");
        } catch (Exception e) {
            logger.info("部署modelId:{}模型服务异常：{}", modelId, e);
            map.put("code", "FAILURE");
        }
        logger.info("流程部署出参map：{}", map);
        return map;
    }

    /**
     * 撤销流程定义
     *
     * @param modelId 模型ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/revokePublish")
    public Object revokePublish(String modelId) {
        logger.info("撤销发布流程入参modelId：{}", modelId);
        Map<String, String> map = new HashMap<String, String>();
        Model modelData = repositoryService.getModel(modelId);
        if (null != modelData) {
            try {
                /**
                 * 参数不加true:为普通删除，如果当前规则下有正在执行的流程，则抛异常
                 * 参数加true:为级联删除,会删除和当前规则相关的所有信息，包括历史
                 */
                repositoryService.deleteDeployment(modelData.getDeploymentId(), true);
                map.put("code", "SUCCESS");
            } catch (Exception e) {
                logger.error("撤销已部署流程服务异常：{}", e);
                map.put("code", "FAILURE");
            }
        }
        logger.info("撤销发布流程出参map：{}", map);
        return map;
    }

    /**
     * 删除流程实例
     *
     * @param modelId 模型ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object deleteProcessInstance(String modelId) {
        logger.info("删除流程实例入参modelId：{}", modelId);
        Map<String, String> map = new HashMap<String, String>();
        Model modelData = repositoryService.getModel(modelId);
        if (null != modelData) {
            try {
                //先删除流程实例，再删除工作流模型
                ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                        processDefinitionKey(modelData.getKey()).singleResult();
                if (null != pi) {
                    runtimeService.deleteProcessInstance(pi.getId(), "");
                    historyService.deleteHistoricProcessInstance(pi.getId());
                }
                //删除流程模型
                repositoryService.deleteModel(modelId);
                map.put("code", "SUCCESS");
            } catch (Exception e) {
                logger.error("删除流程实例服务异常：{}", e);
                map.put("code", "FAILURE");
            }
        }
        logger.info("删除流程实例出参map：{}", map);
        return map;
    }

    /**
     * 手动部署
     * 读取resources目录下process文件bpmn文件
     * @param bpmnName
     * @return
     */
    @GetMapping("excdeploy/{bpmnName}")
    public ResponseResult deploy(@PathVariable("bpmnName") String bpmnName) throws FileNotFoundException {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        // 读取resources目录下process文件bpmn文件
        // deploymentBuilder.addClasspathResource("process/"+bpmnName+".bpmn");
        // 读取指定文件路径下bpmn文件
        String filePath ="D:\\software\\project\\acitiviti7\\src\\main\\resources\\processes\\applyProcess.bpmn";
        InputStream inputStreamBpmn = new FileInputStream(filePath);
        // addInputStream第一个参数就是bpmn文件的全路径，第二个参数bpmn文件流程
        deploymentBuilder.addInputStream(filePath,inputStreamBpmn);
        Deployment deploy = deploymentBuilder.deploy();  // 执行部署
        return ResponseResult.success(deploy);
    }

    /**
     * 导出模型bpmn文件根据模型id
     */
    @RequestMapping ("/export/{modelId}")
    public void export(@PathVariable("modelId") String modelId,HttpServletResponse response) throws IOException {
        Model model = repositoryService.getModel(modelId);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        JsonNode node = objectMapper.readTree(repositoryService.getModelEditorSource(model.getId()));
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(node);
        List<Process> processes = bpmnModel.getProcesses();
        if(CollectionUtils.isEmpty(processes)){
            return;
        }
        BpmnXMLConverter xml = new BpmnXMLConverter();
        byte[] xmlData = xml.convertToXML(bpmnModel);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlData);
        String fileName = bpmnModel.getMainProcess().getId()+".bpmn";
        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        IOUtils.copy(input,response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * 获得流程图 1.子流程 2.结束节点 3.一般节点
     * http://localhost:8088/activitServer/processDiagram?processId=1000
     */
    @RequestMapping("/processDiagram")
    public void processDiagram(String processId, HttpServletResponse response) {

        // showDiagram(processId, response);
    }
   /* public String showDiagram(String processId, HttpServletResponse response) {
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
                logger.error("输出图像失败：", e);
            }
        }
        return null;
    }
    public InputStream getDiagram(String processInstanceId) {
        // 查询流程实例
        ProcessInstance pi = this.runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        // 查询流程实例
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(pi.getProcessDefinitionId()).singleResult();
        // 获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(pd.getId());
        // 定义使用宋体
        String fontName = "宋体";
        // 获取流程实实例当前点的节点，需要高亮显示
        List<String> currentActs = runtimeService.getActiveActivityIds(pi.getId());
        // BPMN模型对象、图片类型、显示的节点
        ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        InputStream is = generator.generateDiagram(model, currentActs, new ArrayList<String>(), fontName, fontName, fontName);
        return is;
    }*/
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
     * 获取部署流程的流程图片
     *
     * http://localhost:8088/activitServer/processDefineByDeployId?deployId=1113
     * @param request
     * @param response
     * @throws IOException
     */
    /*@RequestMapping("processDefineByDeployId")
    public void processDefineByDeploy(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String deployId = request.getParameter("deployId");
       // 获取BPMN模型对象
        ProcessDefinition singleResult = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        BpmnModel model = repositoryService.getBpmnModel(singleResult.getId());
        // BPMN模型对象、图片类型、显示的节点
        String fontName = "宋体";
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        //activiti-engine-5.15.jar版本获取diagramGenerator方式
        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        InputStream is = diagramGenerator.generateDiagram(
                model, new ArrayList<String>(), new ArrayList<String>(), fontName, fontName, fontName);
        // response.setContentType(MULTIPART);
        OutputStream out = null;
        out = response.getOutputStream();
        out.write(getImgByte(is));
        out.flush();
        out.close();
    }
*/
    /**
     * 通过任务获取自动义表单
     * @param taskId
     * @return
     */
    @GetMapping("/getTaskFormKey/{taskId}")
    public @ResponseBody
    ResponseResult getTaskFormKey(@PathVariable String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String formKey = task.getFormKey();
        return ResponseResult.success(formKey);
    }

    /**
     * 通过部署ip获取自动义表单
     * @param taskId
     * @return
     */
    @GetMapping("/getForms/{deployId}")
    public @ResponseBody  ResponseResult getForms(@PathVariable String deployId){
        ProcessDefinition singleResult = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(singleResult.getId());
        /*DefaultFormHandler formHandler;
        if (this.taskDefinitionKey == null) {
            formHandler = (DefaultFormHandler)processDefinition.getStartFormHandler();
        } else {
            TaskDefinition taskDefinition = (TaskDefinition)processDefinition.getTaskDefinitions().get(this.taskDefinitionKey);
            formHandler = (DefaultFormHandler)taskDefinition.getTaskFormHandler();
        }*/
        return null;
    }
    /**
     * 获取流程图，已经处理过高亮显示
     *
     * http://localhost:8088/activitServer/processDefine?processDefinitionId=1310
     * @param request
     * @param response
     * @throws IOException
     */
    //@RequestMapping("processDefine")
   /* public void queryProPlan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String processInstanceId = request.getParameter("processDefinitionId");
        // 获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        // 已执行的节点ID集合:也就是高亮环节id集合
        List<String> executedActivityIdList = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : highLightedActivitList) {
            if ("userTask".equals(activityInstance.getActivityType()) && StringUtils.isEmpty(activityInstance.getEndTime())) {
                executedActivityIdList.add(activityInstance.getActivityId() + "#");
            } else {
                executedActivityIdList.add(activityInstance.getActivityId());
            }
        }
        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        //activiti-engine-5.15.jar版本获取diagramGenerator方式
        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        // 需要亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(bpmnModel, highLightedActivitList);
        String fontName = "宋体";
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, executedActivityIdList, highLightedFlows, fontName, fontName, fontName);
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }

    }
*/
    /**
     * 获取已经流转的线
     *
     * @param bpmnModel
     * @param historicActivityInstances
     * @return
     */
   /* private static List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlowIds = new ArrayList<>();
        // 全部活动节点
        List<FlowNode> historicActivityNodes = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstances = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            FlowNode flowNode =
                    (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            historicActivityNodes.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstances.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode = null;
        FlowNode targetFlowNode = null;
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode =
                    (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();

            *//**
             * 遍历outgoingFlows并找到已已流转的 满足如下条件认为已已流转： 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
             *//*
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicActivityNodes.contains(targetFlowNode)) {
                        highLightedFlowIds.add(targetFlowNode.getId());
                    }
                }
            } else {
                List<Map<String, Object>> tempMapList = new ArrayList<>();
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("highLightedFlowId", sequenceFlow.getId());
                            map.put("highLightedFlowStartTime", historicActivityInstance.getStartTime().getTime());
                            tempMapList.add(map);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(tempMapList)) {
                    // 遍历匹配的集合，取得开始时间最早的一个
                    long earliestStamp = 0L;
                    String highLightedFlowId = null;
                    for (Map<String, Object> map : tempMapList) {
                        long highLightedFlowStartTime = Long.valueOf(map.get("highLightedFlowStartTime").toString());
                        if (earliestStamp == 0 || earliestStamp >= highLightedFlowStartTime) {
                            highLightedFlowId = map.get("highLightedFlowId").toString();
                            earliestStamp = highLightedFlowStartTime;
                        }
                    }

                    highLightedFlowIds.add(highLightedFlowId);
                }

            }

        }
        return highLightedFlowIds;
    }

*/
    /**
     * 导入模型xml根据模型id
     */
    @RequestMapping("/importModel")
    public void importModel(@RequestParam("file") MultipartFile file) throws IOException {
        String processName = file.getOriginalFilename(); // 文件名
        InputStream input = file.getInputStream();
        byte[] bytes = new byte[input.available()];
        input.read(bytes);
        String xml =new String(bytes);
        //创建一个空模型
        Model model = getCreateModel(processName);
        // 保存模型
        repositoryService.saveModel(model);
        // 保存模型中流程数据
        repositoryService.addModelEditorSource(model.getId(), xml.getBytes("utf-8"));
        // 进部署操作
        JsonNode modelNode = new ObjectMapper().readTree(bytes);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .addBpmnModel(model.getKey() + ".bpmn20.xml", bpmnModel)
                .deploy();
        model.setDeploymentId(deployment.getId());
        repositoryService.saveModel(model);
    }
    /**
     * 发布流程 :发布bpmn文件
     * @param deployInfoVO
     * @return
     */
    @RequestMapping(value="/model/deploy", method = RequestMethod.POST)
    public String deployJbpmFile(@RequestBody DeployInfoVO deployInfoVO){
        String name = deployInfoVO.getBpmnName();
        String resourceName = PathUtil.combine(name+ UUIDUtils.get32UUID(), ".bpmn20.xml");
        String jbpmPath = deployInfoVO.getBpmnPath();
        DeploymentResponse deploymentResponse = deployProcess(resourceName, jbpmPath);
        String deployId = deploymentResponse.getId();
        return deployId;
    }
    /**
     * 发布流程
     * processPath：流程路径
     * @param processPath
     */
    public DeploymentResponse deployProcess(String resourceName, String processPath) {
        File file = new File(processPath);
        if(file.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger.error("找不到bpmn文件", e);
            }
            Deployment deploy = repositoryService.createDeployment().addInputStream(resourceName, inputStream).deploy();
            /**
             * activiti7同步bpmn文件部署后，不能生成图片，需要图片上传上去
             * Deployment deployment = repositoryService.createDeployment()//创建Deployment对象
             *             .name('name')
             *             .key('')
             *             .category('')
             *             .tenantId("000000")
             *             .addInputStream(fileBpmn.getName(), BpmnInputStream)//添加bpmn文件
             *             .addInputStream(fileSvg.getName(), SvgInputStream)//添加png/svg文件
             *             .deploy();//部署
             *     log.info("name---" + deployment.getName());
             *
             */
            return new DeploymentResponse(deploy);
        }else {
            throw new RuntimeException("找不到文件"+processPath+"路径");
        }
    }

    private Model getCreateModel(String processName) {
        Model model = repositoryService.newModel();
        model.setName(processName);
        model.setKey(processName);
        model.setVersion(1);
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, processName);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "描述");
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        model.setMetaInfo(modelNode.toString());
        return model;
    }
}
