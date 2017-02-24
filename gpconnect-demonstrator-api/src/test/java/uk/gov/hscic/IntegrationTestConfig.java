package uk.gov.hscic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import uk.gov.hscic.auth.CertificateValidator;
import uk.gov.hscic.auth.KeyStoreFactory;

/**
 *
 * @author Kris Bloe
 */
@SpringBootApplication
public class IntegrationTestConfig {

    @Bean
    public CertificateValidator certificateValidator() throws Exception {
        return new CertificateValidator(KeyStoreFactory.getKeyStore("src/test/resources/Authentication/server.jks", "password"));
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);

        return propertySourcesPlaceholderConfigurer;
    }
}
