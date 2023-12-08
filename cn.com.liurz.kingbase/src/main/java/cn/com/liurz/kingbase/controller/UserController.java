package cn.com.liurz.kingbase.controller;


import cn.com.liurz.kingbase.entity.User;
import cn.com.liurz.kingbase.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value="/findAll")
    public List<Map<String,Object>> findAll(){
        return userService.getAll();
    }

    @GetMapping(value="/findAllPojo")
    public List<User>  findAllPojo(){
        return userService.findAllPojo();
    }

    @GetMapping(value="/findById")
    public User findById(@RequestParam("id")  String id){
        return userService.findById(id);
    }
    @PostMapping(value="/fliterQuery")
    public List<User> fliterQuery(@RequestBody User queryFliter){
        return userService.fliterQuery(queryFliter);
    }
    /**
     * 单个保存
     * {"id": 102,"name": "test3","account": "test3","password": "7123128825a6eac0eaacfd0bacdac781","status": 0,"roleId": "5"}
     * @param user
     * @return
     */
    @PostMapping(value="/save")
    public boolean save(@RequestBody User user){
        userService.save(user);  // ServiceImpl自带的方法
        return true;
    }
    /**
     * 批量数据保存
     * [{"id": 102,"name": "test3","account": "test3","password": "7123128825a6eac0eaacfd0bacdac781","status": 0,"roleId": "5"} ]
     * @param users
     * @return
     */
    @PostMapping(value="/batchSaves")
    public boolean batchSaves(@RequestBody List<User> users){
        userService.batchSaves(users);
        return true;
    }
    /**
     * 存在更新，不存在新增
     * {"id": 102,"name": "test3","account": "test3","password": "7123128825a6eac0eaacfd0bacdac781","status": 0,"roleId": "5"}
     * @param user
     * @return
     */
    @PostMapping(value="/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody User user){
        userService.saveOrUpdate(user);// ServiceImpl自带的方法
        return true;
    }

    /**
     * 根据主键Id更新数据
     *    {"id": 102,"name": "test3","account": "test3","password": "7123128825a6eac0eaacfd0bacdac781","status": 0,"roleId": "5"}
     * @param user
     * @return
     */
    @PostMapping(value="/updateById")
    public boolean updateById(@RequestBody User user){
        userService.updateById(user);// ServiceImpl自带的方法
        return true;
    }
    /**
     * 批量修改插入
     * {"id": 102,"name": "test3","account": "test3","password": "7123128825a6eac0eaacfd0bacdac781","status": 0,"roleId": "5"}
     * @param users
     * @return
     */
    @PostMapping(value="/saveOrUpdateBatch")
    public boolean saveOrUpdateBatch(@RequestBody List<User> users){
        userService.saveOrUpdateBatch(users,users.size());// ServiceImpl自带的方法
        return true;
    }


    /**
     * 通过主键id删除数据
     * @param id
     * @return
     */
    @PostMapping(value="/delete/{id}")
    public boolean saveOrUpdateBatch(@PathVariable("id") String id){
        userService.removeById(id);// ServiceImpl自带的方法
        return true;
    }

    /**
     * 通过条件删除数据
     * @param user
     * @return
     */
    @PostMapping(value="/deleteFilter")
    public boolean deleteFilter(@RequestBody User user){
        QueryWrapper<User> query = new QueryWrapper<User>();
        query.like("name",user.getName());
        userService.remove(query);// ServiceImpl自带的方法
        return true;
    }


}
