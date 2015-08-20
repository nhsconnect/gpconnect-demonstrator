package net.nhs.esb.util;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public abstract class PropertiesTestConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();

        loadDefaultProperties(properties);
        loadProperties(properties);

        configurer.setProperties(properties);
        return configurer;
    }

    protected void loadDefaultProperties(Properties properties) {
    }

    protected abstract void loadProperties(Properties properties);
}
