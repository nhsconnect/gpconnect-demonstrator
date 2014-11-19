package net.nhs.repo.legacy.config;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class LegacyDataConfig {

	@Autowired
	private Environment environment;

	@Bean(destroyMethod="close")
	public DataSource legacyDataSource() {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://localhost:3306/poc_legacy");
		dataSource.setUsername("root");
		dataSource.setPassword(null);
		return dataSource;
	}

}
