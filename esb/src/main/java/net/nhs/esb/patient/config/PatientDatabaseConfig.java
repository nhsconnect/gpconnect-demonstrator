package net.nhs.esb.patient.config;

import java.net.URI;
import java.net.URISyntaxException;
import javax.sql.DataSource;

import com.mysql.jdbc.Driver;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
public class PatientDatabaseConfig {

    @Bean(destroyMethod = "close")
    public DataSource patientDataSource() throws URISyntaxException {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(Driver.class.getName());

        String envString = System.getenv("CLEARDB_DATABASE_URL");

        if (envString != null) {
            URI dbUri = new URI(envString);
            dataSource.setUrl("jdbc:mysql://" + dbUri.getHost() + dbUri.getPath());
            dataSource.setUsername(dbUri.getUserInfo().split(":")[0]);
            dataSource.setPassword(dbUri.getUserInfo().split(":")[1]);
        } else {
            dataSource.setUrl("jdbc:mysql://localhost:3306/poc_patients");
            dataSource.setUsername("root");
            dataSource.setPassword(null);
        }

        return dataSource;
    }
}
