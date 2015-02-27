package net.nhs.esb.patient.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import net.nhs.esb.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = PatientRepository.class,
        entityManagerFactoryRef = "patientEntityManagerFactory",
        transactionManagerRef = "patientTransactionManager"
)
public class PatientRepositoryConfig {

    @Autowired
    private DataSource legacyDataSource;

    @Bean
    public EntityManagerFactory patientEntityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setDatabase(Database.MYSQL);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("net.nhs.esb");
        factory.setDataSource(legacyDataSource);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager patientTransactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(patientEntityManagerFactory());
        return txManager;
    }
}
