package net.nhs.esb.routes;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static net.nhs.domain.openehr.constants.RouteConstants.*;

import java.util.Properties;

import net.nhs.esb.aggregators.ListAddElementAggregator;
import net.nhs.esb.routes.OpenEHRGetDiagnosesForPatientRoute;
import net.nhs.esb.util.PropertiesTestConfig;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		OpenEHRGetDiagnosesForPatientRouteTest.TestConfig.class,
		OpenEHRGetDiagnosesForPatientRoute.class,
		ListAddElementAggregator.class
		}, loader = CamelSpringDelegatingTestContextLoader.class)
@MockEndpoints
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class OpenEHRGetDiagnosesForPatientRouteTest {
	@Produce(uri = DIRECT+START)
	private ProducerTemplate start;

	@EndpointInject(uri = MOCK+DIRECT+OPENEHR_GET_DIAGNOSES_FOR_PATIENT_ROUTE)
	private MockEndpoint openEHRGetDiagnosesForPatientRouteMock;
	
	@EndpointInject(uri = MOCK+END)
	private MockEndpoint endMock;

    @Ignore
    @Test
	public void testRoute() throws Exception {
		// given
        //given(diagnosesRepository.findAll()).willReturn(null);

        // when
        start.sendBody(null);

        // then
        assertEquals(1, openEHRGetDiagnosesForPatientRouteMock.getExchanges().size());
        assertEquals(1, endMock.getExchanges().size());
	}

    @Configuration
	public static class TestConfig extends PropertiesTestConfig {

        @Override
        protected void loadProperties(Properties properties) {
			properties.put("openEHRGetDiagnosesForPatientRoute", DIRECT+OPENEHR_GET_DIAGNOSES_FOR_PATIENT_ROUTE);
        }

//        @Bean
//        public DiagnosesRepository getDiagnosesRepository() {
//            return diagnosesRepository;
//        }
        
        @Bean
		public SpringRouteBuilder testRoute() {
			return new SpringRouteBuilder() {

				@Override
				public void configure() throws Exception {
                    from(DIRECT+START)
                    	.to(DIRECT+OPENEHR_GET_DIAGNOSES_FOR_PATIENT_ROUTE)
                        .to(MOCK+END)
                        ;
				}
			};
		}
	}
}
