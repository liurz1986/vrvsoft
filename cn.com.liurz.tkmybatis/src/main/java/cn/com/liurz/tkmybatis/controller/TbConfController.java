package cn.com.liurz.tkmybatis.controller;

import cn.com.liurz.tkmybatis.entity.TbConf;
import cn.com.liurz.tkmybatis.service.TbConfService;
import cn.com.liurz.tkmybatis.util.ResponseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tbConf")
@Api(tags="配置表信息 API",description = "配置表信息 API")
public class TbConfController {

    /**
     * 通过tkmybatis不写sql的方式查询
     */
    @Autowired
    private TbConfService tbConfService;
    @GetMapping("/getOne")
    @ApiOperation(value = "通过tkmybatis不写sql的方式查询", notes = "通过tkmybatis不写sql的方式查询1")
    public ResponseResult getOne(@RequestParam("confId") String confId){
        return ResponseResult.success(tbConfService.getOneByConfId(confId));
    }

    /**
     * 通过tkmybatis不写sql的方式查询
     * @param confId
     * @return
     */
    @GetMapping("/findByCondition")
    @ApiOperation(value = "通过tkmybatis不写sql的方式查询", notes = "通过tkmybatis不写sql的方式查询1")
    public ResponseResult findByCondition(@RequestParam("confId") String confId){
        TbConf conf = new TbConf();
        conf.setConfId(confId);
        return ResponseResult.success(tbConfService.findByCondition(conf));
    }

    /**
     * 通过mybatis写sql的方式
     * @return
     */
    @GetMapping("/findAll")
    @ApiOperation(value = "通过mybatis写sql的方式", notes = "通过mybatis写sql的方式1")
    public ResponseResult findAll(){
        return ResponseResult.success(tbConfService.getAll());
    }

}
