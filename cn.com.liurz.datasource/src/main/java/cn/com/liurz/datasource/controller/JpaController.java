package cn.com.liurz.datasource.jpa.controller;
import cn.com.liurz.datasource.jpa.db1.entity.AssetBean;
import cn.com.liurz.datasource.jpa.db1.repository.Db1DataTestRepository;
import cn.com.liurz.datasource.jpa.db2.Repository.Db2DataTestRepository;
import cn.com.liurz.datasource.jpa.db2.entity.DataSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * 多数源测试
 */

@RestController
@RequestMapping("/jpatest")
public class JpaController {

    @Autowired
    private Db1DataTestRepository db1DataTestRepository;
    @Autowired
    private Db2DataTestRepository db2DataTestRepository;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate primaryJdbcTemplate;

    @Autowired
    @Qualifier("secondJdbcTemplate")
    private JdbcTemplate secondJdbcTemplate;
    /**
     * 通过jpa获取db1数据库数据
     * @return
     */
    @GetMapping(value="/db1Config")
    public List<DataSourceBean> jpadb1Config() {
        return db2DataTestRepository.findAll();
    }

    /**
     * 通过jpa获取默认数据库数据
     * @return
     */
    @GetMapping(value="/db2Config")
    public List<AssetBean> getJpadb2Config() {
        return db1DataTestRepository.findAll();
    }

    /**
     * 通过JdbcTemplate获取db2数据库数据
     * @return
     */
    @GetMapping(value="/jdbcTemplateConfig2")
    public List<Map<String, Object>> jdbcTemplatedb1Config() {
        return secondJdbcTemplate.queryForList("select * from data_source");
    }

    /**
     * 通过JdbcTemplate获取默认数据库数据
     * @return
     */
    @GetMapping(value="/jdbcTemplateConfig1")
    public List<Map<String, Object>> jdbcTemplatedb2Config() {
        return primaryJdbcTemplate.queryForList("select Guid as guid,name as name from asset");
    }
}
