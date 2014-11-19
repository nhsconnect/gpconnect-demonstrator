package net.nhs.esb.config;

import net.nhs.esb.routes.OpenEHRGetDiagnosesForPatientRoute;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { OpenEHRGetDiagnosesForPatientRoute.class })
public class RouteConfig {
}
