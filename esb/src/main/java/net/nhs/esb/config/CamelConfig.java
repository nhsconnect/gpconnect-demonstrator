package net.nhs.esb.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.nhs.esb.util.CamelConverterInjector;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig extends CamelConfiguration {

    @Bean
    @Override
    public List<RouteBuilder> routes() {
        Collection<RouteBuilder> routeBuilders = getApplicationContext().getBeansOfType(RouteBuilder.class).values();
        return new ArrayList<>(routeBuilders);
    }

    @Bean
    public CamelConverterInjector camelConverterInjector() {
        return new CamelConverterInjector();
    }
}
