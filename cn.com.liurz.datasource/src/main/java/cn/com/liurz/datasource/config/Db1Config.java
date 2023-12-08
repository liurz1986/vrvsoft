package cn.com.liurz.datasource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * 手动配置jpa多数据源
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef="db1EntityManagerFactoryBeanPrimary", //实体类工厂依赖
transactionManagerRef ="db1TransactionManagerPrimary" , // 事务依赖
        basePackages ={"cn.com.liurz.datasource.db1.repository"}) // repository所在的包
@Slf4j
public class Db1Config{
    @Autowired
    @Qualifier("primaryDataSource")
    private  DataSource primaryDataSource;



    @Primary
    @Bean(name = "db1EntityManagerPrimary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return db1EntityManagerFactoryBeanPrimary(builder).getObject().createEntityManager();
    }
    /**
     * 配置扫描实体
     * @param builder
     * @return
     */
    @Primary
    @Bean(name = "db1EntityManagerFactoryBeanPrimary")
    public LocalContainerEntityManagerFactoryBean db1EntityManagerFactoryBeanPrimary(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(primaryDataSource)
                .packages("cn.com.liurz.datasource.db1.entity")
                .persistenceUnit("db1persistenceUnit")
                .build();
    }
    @Primary
    @Bean(name = "db1TransactionManagerPrimary")
    public PlatformTransactionManager db1TransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(db1EntityManagerFactoryBeanPrimary(builder).getObject());
    }
}
