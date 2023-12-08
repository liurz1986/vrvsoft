package cn.com.liurz.kingbase;

import cn.com.liurz.kingbase.entity.User;
import cn.com.liurz.kingbase.mapper.UserMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class test1 {
    private Logger logger = LoggerFactory.getLogger(test1.class);

    @Autowired
    private UserMapper userMapper;

    /**
     *   这里all_user表的id是自增，所以只传入name
     */
    @Test
    void save(){
        User user = new User();
        user.setId(101);
        user.setName("testName");
        userMapper.insert(user);
        this.selectAll();
    }

    /**
     * 通过id查询功能
     */
    @Test
    void select() {
        QueryWrapper<User> query = new QueryWrapper<User>();
        query.eq("id",101);
        List<User> select = userMapper.selectList(query);
        logger.info(JSON.toJSONString(select));

    }

    /**
     * 查询全部
     */
    @Test
    void selectAll(){
        List<User> select = userMapper.selectList(new QueryWrapper<User>());
    }
    /**
     * 通过id进行修改
     */
    @Test
    void update(){
        User user = new User();
        user.setId(101);
        user.setName("testName111");
        userMapper.update(user,null);
    }

    /**
     * 通过id删除
     */
    @Test
    void delete(){
        QueryWrapper<User> query = new QueryWrapper<User>();
        query.eq("id",32);
        userMapper.delete(query);
    }
}
