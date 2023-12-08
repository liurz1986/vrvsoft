package cn.com.liurz.datasource.jpa.db1.repository;

import cn.com.liurz.datasource.jpa.db1.entity.AssetBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Db1DataTestRepository extends JpaRepository<AssetBean, String> {
}
