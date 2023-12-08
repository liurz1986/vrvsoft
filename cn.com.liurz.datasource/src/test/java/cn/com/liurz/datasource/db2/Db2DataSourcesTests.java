/*
package cn.com.liurz.datasource.db2;

import cn.com.liurz.datasource.db2.Repository.Db2DataTestRepository;
import cn.com.liurz.datasource.model.DataSourceBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Db2DataSourcesTests {
    @Autowired
    private Db2DataTestRepository db2DataTestRepository;

    @Test
    @Transactional("db2TransactionManager")
    public void createCustomer() {
        List<DataSourceBean> list = db2DataTestRepository.findAll();
    }
}
*/
