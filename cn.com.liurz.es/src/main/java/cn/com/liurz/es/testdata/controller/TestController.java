package cn.com.liurz.es.testdata.controller;
import cn.com.liurz.es.testdata.service.TestAggregationService;
import cn.com.liurz.es.testdata.service.TestServiceImpl;
import cn.com.liurz.es.testdata.entity.TestEntity;

import cn.com.liurz.es.util.QueryCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testes")
public class TestController {

    @Autowired
    private TestServiceImpl testService;
    @Autowired
    private TestAggregationService testAggregationService;

    @PostMapping("/save")
    public boolean getOne(@RequestBody TestEntity entity){
        testService.save(entity);
        return true;
    }
    /*
     [
 { "id": "12","message": "12fasdfadsfasd"},
  { "id": "13","message": "12fasdfadsfasd2"},
   { "id": "14","message": "12fasdfadsfasd3"}
 ]
     */
    @PostMapping("/batchSave")
    public boolean batchSave(@RequestBody List<TestEntity> entitys){

        testService.addList(entitys);
        return true;
    }
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable("id") String id) {
        testService.deleteById(id);
    }

    @PostMapping("/findAll")
    public List<TestEntity> findAll(){

         return testService.findAll();
    }

    /**
     *  { "id": "2","message": "12fasdfadsfasd2"}
     * @param testEntity
     * @return
     */
    @PostMapping("/findCondition")
    public List<TestEntity> findCondition(@RequestBody TestEntity testEntity){
        List<QueryCondition> conditions = new ArrayList<>();
        if(StringUtils.isNotEmpty(testEntity.getId())){
            conditions.add(QueryCondition.eq("id",testEntity.getId()));
        }
        if(StringUtils.isNotEmpty(testEntity.getMessage())){
            conditions.add(QueryCondition.eq("message",testEntity.getMessage()));
        }
        return testService.findAll(conditions);
    }

    @PostMapping("/getAageMin")
    public double getAageMin(){
        return testAggregationService.getAageMin();
    }

    @PostMapping("/getAageMax")
    public double getAageMax(){
        return testAggregationService.getAageMax();
    }
    @PostMapping("/getAageAvg")
    public double getAageAvg(){
        return testAggregationService.getAageAvg();
    }
    @PostMapping("/getAageSum")
    public double getAageSum(){
        return testAggregationService.getAageSum();
    }
    @PostMapping("/getAageCount")
    public double getAageCount(){
        return testAggregationService.getAageCount();
    }
    @PostMapping("/getAageStats")
    public Map<String,Object> getAageStats(){
        return testAggregationService.getAageStats();
    }
    @PostMapping("/getDocById")
    public Map<String,Object> getDocById(String id) throws IOException {
        return testAggregationService.getDocById(id);
    }

}
