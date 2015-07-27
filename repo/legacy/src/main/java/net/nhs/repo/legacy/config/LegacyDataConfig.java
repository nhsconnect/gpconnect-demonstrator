package net.nhs.repo.legacy.config;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class LegacyDataConfig {

	@Autowired
	private Environment environment;

	@Bean(destroyMethod="close")
	public DataSource legacyDataSource() throws URISyntaxException {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());

		String envString = System.getenv("CLEARDB_DATABASE_URL");

		if (envString != null) {
			URI dbUri = new URI(envString);
			dataSource.setUrl("jdbc:mysql://" + dbUri.getHost() + dbUri.getPath());
			dataSource.setUsername(dbUri.getUserInfo().split(":")[0]);
			dataSource.setPassword(dbUri.getUserInfo().split(":")[1]);
		} else {
			dataSource.setUrl("jdbc:mysql://localhost/poc_legacy");
			dataSource.setUsername("root");
			dataSource.setPassword(null);
		}

		return dataSource;
	}

}
