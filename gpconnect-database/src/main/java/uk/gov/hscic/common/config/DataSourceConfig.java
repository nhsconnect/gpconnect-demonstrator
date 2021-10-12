package uk.gov.hscic.common.config;

import com.mysql.jdbc.Driver;
import javax.persistence.EntityManagerFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = "uk.gov.hscic")
public class DataSourceConfig {

    @Value("${datasource.vendor:mysql}")
    private String vendor;

    @Value("${datasource.host}")
    private String host;

    @Value("${datasource.port}")
    private String port;

    @Value("${datasource.schema:gpconnect}")
    private String schema;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;

    @Value("${datasource.showSql:false}")
    private boolean showSql;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        final DataSource dataSource = new DataSource();

        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUrl("jdbc:" + vendor + "://" + host + ":" + port + "/" + schema + "?useSSL=false");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setValidationQuery("select 1 as dbcp_connection_test");
        dataSource.setTestOnBorrow(true);

        return dataSource;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        final Database database = Database.valueOf(vendor.toUpperCase());

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(showSql);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabase(database);

        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("uk.gov.hscic");
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public RefreshData getRefreshData() {
        return new RefreshData();
    }
}
