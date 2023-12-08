package cn.com.liurz.flow.controller;


/*import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;*/
import cn.com.liurz.flow.util.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("flow")
public class FlowCotroller {
    // Activiti7 新接口由于内置SpringSecurity，在运行时会报错
  /*  @Autowired
    private ProcessRuntime processRuntime ;*/


    /**
     * 启动流程
     * @param
     * @return
     */
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public ResponseResult startProcess(){
        // SecurityUtil.logInAs("admin");
        // activiti在操作ProcessRuntime时要加上鉴权参数
      /*  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> variables = new HashMap<>();
        variables.put("sponsor", authentication.getName());
        StartProcessPayload param = ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("testMainProcess")  // 对应act_re_procdef中的key_
                .withName("流程名称")
                .withBusinessKey("自定义业务key")
                .withVariables(variables).build();*/
        /**
         *    StartProcessPayload对象的实体
         *     private String id;
         *     private String processDefinitionId;     // 对应act_re_procdef中的id_
         *     private String processDefinitionKey;  // 对应act_re_procdef中的key_
         *     private String name;    // 流程名称
         *     private String businessKey; // 自定义业务key
         *     private Map<String, Object> variables; // 流程参数
         *
         *     启动参数processDefinitionId和processDefinitionKey一个就可以了
         *     流程名称和自定义业务key不起什么作用
         */
      /*  ProcessInstance instance = processRuntime.start(param);  // 报错403
        //ProcessInstance instance =runtimeService.startProcessInstanceByKey("testMainProcess");
        return ResponseResult.success(instance);*/
        return null;
    }
}
