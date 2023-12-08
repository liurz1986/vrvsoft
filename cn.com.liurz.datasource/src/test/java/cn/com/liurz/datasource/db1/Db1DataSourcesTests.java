package cn.com.liurz.datasource.db1;

import cn.com.liurz.datasource.jpa.db1.entity.AssetBean;
import cn.com.liurz.datasource.jpa.db1.entity.repository.Db1DataTestRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @RunWith(SpringRunner.class)
@SpringBootTest
public class Db1DataSourcesTests {
    @Autowired
    private Db1DataTestRepository db1DataTestRepository;
    @Test
    @Transactional("db1TransactionManager")
    public void createCustomer() {
        List<AssetBean> list = db1DataTestRepository.findAll();
    }

}
