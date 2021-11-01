package uk.gov.hscic.common.config;

import com.mysql.jdbc.Driver;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class LegacyDataConfig {

    @Value("${legacy.datasource.vendor:mysql}")
    private String vendor;

    @Value("${legacy.datasource.host:127.0.0.1}")
    private String host;

    @Value("${legacy.datasource.port:3306}")
    private String port;

    @Value("${legacy.datasource.schema:poc_legacy}")
    private String schema;

    @Value("${legacy.datasource.username:root}")
    private String username;

    @Value("${legacy.datasource.password:password}")
    private String password;

    @Bean(destroyMethod = "close")
    public DataSource legacyDataSource() {
        final BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl("jdbc:" + vendor + "://" + host + ":" + port + "/" + schema + "?useSSL=false");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setValidationQuery("select 1 as dbcp_connection_test");
        dataSource.setTestOnBorrow(true);

        return dataSource;
    }

    @Bean
    public RefreshData getRefreshData() {
        return new RefreshData();
    }
}
