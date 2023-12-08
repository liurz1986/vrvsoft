package cn.com.liurz.flow.controller;

import cn.com.liurz.flow.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

@Slf4j
@RestController
@RequestMapping("/deploy")
public class DeploymentController {
    @Autowired
    private RepositoryService repositoryService;
    /**
     * 部署
     * @param file  ZIP压缩包文件
     * @param processName   流程名称
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("zipFile") MultipartFile file, @RequestParam("processName") String processName) {
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith("zip")) {
            return ResponseResult.error("文件格式错误");
        }
        ProcessDefinition processDefinition = null;
        try {
            ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
            Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).name(processName).deploy();
            processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        } catch (IOException e) {
            log.error("流程部署失败！原因: {}", e.getMessage(), e);
        }
        return ResponseResult.success(processDefinition.getId());
    }
    /**
     * 查看流程图
     * @param deploymentId  部署ID
     * @param resourceName  图片名称
     * @param response
     * @return
     */
    @GetMapping("/getDiagram")
    public void getDiagram(@RequestParam("deploymentId") String deploymentId, @RequestParam("resourceName") String resourceName, HttpServletResponse response) {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
//        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try {
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
