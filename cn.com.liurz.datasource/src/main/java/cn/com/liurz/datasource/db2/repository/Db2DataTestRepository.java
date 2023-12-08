package cn.com.liurz.datasource.jpa.db2.Repository;

import cn.com.liurz.datasource.jpa.db2.entity.DataSourceBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Db2DataTestRepository extends JpaRepository<DataSourceBean, Long> {
}
