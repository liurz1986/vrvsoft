package cn.com.liurz.tkmybatis.controller;

import cn.com.liurz.tkmybatis.util.ResponseResult;

import cn.com.liurz.tkmybatis.util.WriteDataBySqlFile;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testSql")
public class WriteDataBySqlFileController {
    @Autowired
    private WriteDataBySqlFile WriteDataBySqlFile;

    @PostMapping("/exc")
    @ApiOperation(value = "getOne")
    public ResponseResult getOne(){
        WriteDataBySqlFile.exc("D:\\test_data.sql");
        return ResponseResult.success(true);
    }
}
