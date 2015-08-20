package net.nhs.esb.config;

import net.nhs.esb.util.CamelConverterInjector;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig extends CamelConfiguration {

    @Bean
    public CamelConverterInjector camelConverterInjector() {
        return new CamelConverterInjector();
    }
}
