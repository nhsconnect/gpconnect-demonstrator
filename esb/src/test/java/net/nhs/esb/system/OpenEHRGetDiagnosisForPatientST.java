package net.nhs.esb.system;

import static net.nhs.domain.openehr.constants.RouteConstants.DIRECT;
import static net.nhs.domain.openehr.constants.RouteConstants.END;
import static net.nhs.domain.openehr.constants.RouteConstants.MOCK;
import static net.nhs.domain.openehr.constants.RouteConstants.OPENEHR_GET_DIAGNOSIS_FOR_PATIENT_ROUTE;
import static net.nhs.domain.openehr.constants.RouteConstants.START;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.nhs.esb.aggregators.ListAddElementAggregator;
import net.nhs.esb.config.RestConfig;
import net.nhs.esb.enrichers.DiagnosesEnricher;
import net.nhs.esb.enrichers.OpenEhrPatientParams;
import net.nhs.esb.rest.Patients;
import net.nhs.esb.routes.OpenEHRCommonRoute;
import net.nhs.esb.routes.OpenEHRGetDiagnosisForPatientRoute;
import net.nhs.esb.util.PropertiesTestConfig;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
                                 OpenEHRGetDiagnosisForPatientST.TestConfig.class,
                                 OpenEHRGetDiagnosisForPatientRoute.class,
                                 OpenEhrPatientParams.class,
                                 OpenEHRCommonRoute.class,
                                 RestConfig.class,
                                 DiagnosesEnricher.class,
                                 ListAddElementAggregator.class
}, loader = CamelSpringDelegatingTestContextLoader.class)
@MockEndpoints
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class OpenEHRGetDiagnosisForPatientST {

    @Produce(uri = DIRECT + START)
    private ProducerTemplate start;

    @EndpointInject(uri = MOCK + END)
    private MockEndpoint endMock;

    @Test
    public void testRoute() throws Exception {
        //given
        Integer patientId = 746;
        String diagnosisId = "e774b157-c908-4dfa-b30d-4f9a65ca539b::handi.ehrscape.com::3";
        Map<String, Object> headers = new HashMap<>();
        headers.put(Patients.PATIENT_ID_HEADER, patientId);
        headers.put(Patients.DIAGNOSIS_ID_HEADER, diagnosisId);

        // when
        start.sendBodyAndHeaders(null, headers);

        // then
        assertEquals(1, endMock.getExchanges().size());
    }

    @Configuration
    public static class TestConfig extends PropertiesTestConfig {

        @Override
        protected void loadProperties(Properties properties) {
            properties.put("openEHRGetDiagnosisForPatientRoute", DIRECT + OPENEHR_GET_DIAGNOSIS_FOR_PATIENT_ROUTE);
        }

        @Bean
        public SpringRouteBuilder testRoute() {
            return new SpringRouteBuilder() {

                @Override
                public void configure() throws Exception {
                    from(DIRECT + START)
                     .to(DIRECT + OPENEHR_GET_DIAGNOSIS_FOR_PATIENT_ROUTE)
                     .to("log:end?showAll=true")
                     .to(MOCK + END)
                    ;
                }
            };
        }
    }
}
