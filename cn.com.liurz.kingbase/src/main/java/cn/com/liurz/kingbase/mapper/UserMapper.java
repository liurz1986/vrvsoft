package cn.com.liurz.kingbase.mapper;
import cn.com.liurz.kingbase.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * cn.com.liurz.plus.mapper.AppSysManagerMapper
 * @author vrv
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    public List<Map<String,Object>> findAll();

    public  List<User>  findPojo();

    public void  insertUsers(List<User> userList);

}
