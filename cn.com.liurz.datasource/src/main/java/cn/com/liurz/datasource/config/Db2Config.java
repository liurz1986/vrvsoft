package cn.com.liurz.datasource.jpa.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
/**
 * 手动配置jpa多数据源
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef="db2EntityManagerFactory", //实体类工厂依赖
transactionManagerRef ="db2TransactionManager" , // 事务依赖
        basePackages ={"cn.com.liurz.datasource.db2.repository"}) // repository所在的包
@EnableTransactionManagement
@Slf4j
public class Db2Config {
    @Autowired
    @Qualifier("secondDatasource")
    private  DataSource secondDatasource;


    @Bean(name = "db2EntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean db2EntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(secondDatasource).packages("cn.com.liurz.datasource.db2.entity").persistenceUnit("db2persistenceUnit").build();
    }

    @Bean(name = "db2EntityManagerFactory")
    public EntityManagerFactory db2EntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return this.db2EntityManagerFactoryBean(builder).getObject();
    }

    @Bean(name = "db2TransactionManager")
    public PlatformTransactionManager db2TransactionManager(@Qualifier("db2EntityManagerFactory") EntityManagerFactory db2EntityManagerFactory) {
        return new JpaTransactionManager(db2EntityManagerFactory);
    }
}
