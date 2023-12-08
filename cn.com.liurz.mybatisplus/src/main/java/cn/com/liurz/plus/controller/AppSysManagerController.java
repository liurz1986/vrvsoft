package cn.com.liurz.plus.controller;

import cn.com.liurz.plus.entity.AppSysManager;
import cn.com.liurz.plus.service.AppSysManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Api(value = "测试")
@RestController
@RequestMapping(value="/appSysManager")
public class AppSysManagerController {
    @Autowired
    private AppSysManagerService appSysManagerService;
    @ApiOperation(value="查询所有")
    @GetMapping(value="/findAll")
    public List<Map<String,Object>> findAll(){
        return appSysManagerService.getAll();
    }

    @GetMapping(value="/findAllPojo")
    public List<AppSysManager>  findAllPojo(){
        return appSysManagerService.findAllPojo();
    }

    @GetMapping(value="/findById")
    public AppSysManager findById(@RequestParam("id")  String id){
        return appSysManagerService.findById(id);
    }
    @PostMapping(value="/fliterQuery")
    public List<AppSysManager> fliterQuery(@RequestBody AppSysManager queryFliter){
        return appSysManagerService.fliterQuery(queryFliter);
    }
 }
