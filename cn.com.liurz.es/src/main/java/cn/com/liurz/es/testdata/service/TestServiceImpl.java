package cn.com.liurz.es.testdata.service;

import cn.com.liurz.es.testdata.entity.TestEntity;
import org.springframework.stereotype.Service;
import cn.com.liurz.es.common.ElasticSearchRestClientService;

@Service
public class TestServiceImpl extends ElasticSearchRestClientService<TestEntity> {
    public static final String TEST_ENTITY_INDEX = "test_entity_index";

    @Override
    public String getIndexName() {
        return TEST_ENTITY_INDEX;
    }

}
