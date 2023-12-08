package cn.com.liurz.kingbase.service;

import cn.com.liurz.kingbase.entity.User;
import cn.com.liurz.kingbase.mapper.UserMapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author vrv
 */
@Service
@Transactional
public class UserService  extends ServiceImpl<UserMapper,User>{
    @Autowired
    private UserMapper userMapper;

    public List<Map<String,Object>> getAll(){
        return userMapper.findAll();
    }

    public List<User>  findAllPojo(){
        return userMapper.findPojo();
    }

    public User findById(String id){
        /*QueryWrapper<User> query = new QueryWrapper<User>();
        query.eq("id",id);
        User user = this.getOne(query); // ServiceImpl自带的方法*/
        return userMapper.selectById(id);
    }

    /**
     * QueryWrapper封装了进行查询
     *        查询的字段为表的字段
     * @param queryFliter
     * @return
     */
    public List<User> fliterQuery(User queryFliter){
        QueryWrapper<User> query = new QueryWrapper<User>();
        query.like("name",queryFliter.getName());
        query.eq("id",queryFliter.getId());
        return userMapper.selectList(query);
    }


    public void batchSaves(List<User> users) {
        //  userMapper.insertUsers(users);  // 手写的批量保存\
        // this.saveBatch(users);    // ServiceImpl自带的批量保存,这个方法默认保存一千条数据
        this.saveBatch(users,users.size()); //  ServiceImpl自带的批量保存,这个方法设置批量保存的数量

    }
}
