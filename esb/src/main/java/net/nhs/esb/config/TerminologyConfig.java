package net.nhs.esb.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import net.nhs.esb.terminology.rest.LocalTerminologyController;
import org.apache.camel.component.cxf.spring.SpringJAXRSServerFactoryBean;
import org.apache.cxf.endpoint.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
public class TerminologyConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JacksonJsonProvider jacksonJsonProvider;

    @Autowired
    private LocalTerminologyController localTerminologyController;

    @Bean
    public Server localTerminologyServer() {
        SpringJAXRSServerFactoryBean springJAXRSServerFactoryBean = new SpringJAXRSServerFactoryBean();
        springJAXRSServerFactoryBean.setApplicationContext(applicationContext);
        springJAXRSServerFactoryBean.setServiceBeanObjects(localTerminologyController);
        springJAXRSServerFactoryBean.setAddress("/terminology/");
        springJAXRSServerFactoryBean.setLoggingFeatureEnabled(true);
        springJAXRSServerFactoryBean.setProvider(jacksonJsonProvider);
        return springJAXRSServerFactoryBean.create();
    }
}
