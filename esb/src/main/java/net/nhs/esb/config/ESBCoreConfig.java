package net.nhs.esb.config;

import net.nhs.esb.converters.DiagnosesTypeConverter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { DiagnosesTypeConverter.class })
public class ESBCoreConfig {
}
