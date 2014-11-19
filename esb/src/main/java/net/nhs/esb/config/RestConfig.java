package net.nhs.esb.config;

import net.nhs.esb.rest.OpenEhr;
import net.nhs.esb.rest.Patients;

import org.apache.camel.component.cxf.spring.SpringJAXRSClientFactoryBean;
import org.apache.camel.component.cxf.spring.SpringJAXRSServerFactoryBean;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@ComponentScan(basePackageClasses = { Patients.class })
public class RestConfig extends CamelConfiguration {
	private static final String OPENEHR_ADDRESS = "https://rest.ehrscape.com/rest/v1";
	//private static final String OPENEHR_ADDRESS = "http://ocean-db.cloudapp.net";
	//private static final String OPENEHR_ADDRESS = "http://localhost:1234/rest/v1";
	
	@Autowired
	private Patients patients;

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new ISO8601DateFormat());
		return objectMapper;
	}
	
	@Bean
	public JacksonJsonProvider jacksonJsonProvider() {
		JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
		jacksonJsonProvider.setMapper(objectMapper());
		return jacksonJsonProvider;
	}
	
	@Bean
	public SpringJAXRSServerFactoryBean rsPatients() {
		SpringJAXRSServerFactoryBean springJAXRSServerFactoryBean = new SpringJAXRSServerFactoryBean();
		springJAXRSServerFactoryBean.setServiceBean(patients);
		springJAXRSServerFactoryBean.setAddress("/patients/");
		springJAXRSServerFactoryBean.setLoggingFeatureEnabled(true);
		springJAXRSServerFactoryBean.setProvider(jacksonJsonProvider());
		return springJAXRSServerFactoryBean;
	}
	
	@Bean
	public SpringJAXRSClientFactoryBean rsOpenEhr() {
		SpringJAXRSClientFactoryBean springJAXRSClientFactoryBean = new SpringJAXRSClientFactoryBean();
		springJAXRSClientFactoryBean.setServiceClass(OpenEhr.class);
		springJAXRSClientFactoryBean.setAddress(OPENEHR_ADDRESS);
		springJAXRSClientFactoryBean.setLoggingFeatureEnabled(true);
		springJAXRSClientFactoryBean.setProvider(jacksonJsonProvider());
		return springJAXRSClientFactoryBean;
	}
}
