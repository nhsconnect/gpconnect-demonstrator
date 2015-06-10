
package net.nhs.esb.config;

import net.nhs.esb.patient.config.PatientRepositoryConfig;
import net.nhs.repo.legacy.config.LegacyDataConfig;
import net.nhs.repo.legacy.config.LegacyJPATransactionalConfig;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("net.nhs.esb")
@PropertySource("classpath:application.properties")
@Import({
        CamelConfig.class, RestConfig.class,
        LegacyJPATransactionalConfig.class, LegacyDataConfig.class,
        PatientRepositoryConfig.class
})
public class ESBConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Namespaces namespaces() {
        Namespaces namespaces = new Namespaces("soap", "http://schemas.xmlsoap.org/soap/envelope/");
        namespaces.add("wsa", "http://www.w3.org/2005/08/addressing");
        namespaces.add("wsaspine", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
        namespaces.add("itk", "urn:nhs-itk:ns:201005");
        namespaces.add("local", "local-namespace-uri");
        namespaces.add("hl7", "urn:hl7-org:v3");
        namespaces.add("eb", "http://www.oasis-open.org/committees/ebxml-msg/schema/msg-header-2_0.xsd");
        namespaces.add("npfitlc", "NPFIT:HL7:Localisation");

        return namespaces;
    }
}