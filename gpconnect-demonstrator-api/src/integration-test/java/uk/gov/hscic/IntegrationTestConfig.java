package uk.gov.hscic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import uk.gov.hscic.auth.KeyStoreFactory;
import uk.gov.hscic.auth.SignedHandler;
import uk.gov.hscic.common.ldap.EndpointResolver;

@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = EndpointResolver.class)
public class IntegrationTestConfig extends WebMvcConfigurerAdapter {

    @Bean
    public SignedHandler signedHandler() throws Exception {
        return new SignedHandler(KeyStoreFactory.getKeyStore("src/integration-test/resources/authentication/server.jks", "password"));
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);

        return propertySourcesPlaceholderConfigurer;
    }
}
