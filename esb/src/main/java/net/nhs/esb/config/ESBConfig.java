package net.nhs.esb.config;

import java.io.IOException;

import net.nhs.esb.aggregators.ListAddListAggregator;
import net.nhs.esb.enrichers.DiagnosesIdHeaderEnricher;
import net.nhs.repo.legacy.config.LegacyDataConfig;
import net.nhs.repo.legacy.config.LegacyJPATransactionalConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackageClasses = { DiagnosesIdHeaderEnricher.class, ListAddListAggregator.class })
@PropertySource("classpath:application.properties")
@Import({
	ESBCoreConfig.class,
	CamelConfig.class, RouteConfig.class, RestConfig.class,
	LegacyJPATransactionalConfig.class, LegacyDataConfig.class
	})
public class ESBConfig {

	@Autowired
	private Environment environment;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
