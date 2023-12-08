package cn.com.liurz.datasource.jpa.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

	@Bean(name = "primaryDataSource")
	@Qualifier("primaryDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.db1")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
    }
	
	@Bean(name = "secondDatasource")
	@Qualifier("secondDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource secondDataSource() {
		return DataSourceBuilder.create().build();
    }

	/**
	 * 配置对应jdbctemplate
	 * @param dataSource
	 * @return
	 */
	@Bean(name = "primaryJdbcTemplate")
	public JdbcTemplate primaryJdbcTemplate( @Qualifier("primaryDataSource") DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}

	@Bean(name = "secondJdbcTemplate")
	public JdbcTemplate secondJdbcTemplate(@Qualifier("secondDatasource") DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}
}

