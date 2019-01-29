package uk.gov.hscic.common.config;

import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hscic.SystemHeader;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.appointments.ScheduleResourceProvider;
import uk.gov.hscic.common.filters.FhirRequestAuthInterceptor;
import uk.gov.hscic.common.filters.FhirRequestGenericIntercepter;
import uk.gov.hscic.common.filters.PatientJwtValidator;
import uk.gov.hscic.location.LocationResourceProvider;
import uk.gov.hscic.medications.MedicationResourceProvider;
import uk.gov.hscic.metadata.GpConnectServerCapabilityStatementProvider;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.PatientResourceProvider;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;
import uk.gov.hscic.slots.SlotResourceProvider;

@Configuration
@WebServlet(urlPatterns = {"/fhir/*"}, displayName = "FHIR Server")
public class FhirRestfulServlet extends RestfulServer {
    private static final long serialVersionUID = 1L;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${serverBaseUrl}")
    private String serverBaseUrl;
    
    @Override
    protected void initialize() throws ServletException {
        
        FhirContext ctx = FhirContext.forDstu3();
        ctx.setParserErrorHandler(new StrictErrorHandler());
        setFhirContext(ctx);
        setETagSupport(ETagSupportEnum.ENABLED);
       
        setServerAddressStrategy(new HardcodedServerAddressStrategy(serverBaseUrl));
        
        setResourceProviders(Arrays.asList(
                applicationContext.getBean(PatientResourceProvider.class),
                applicationContext.getBean(OrganizationResourceProvider.class),
                applicationContext.getBean(PractitionerResourceProvider.class),
                //applicationContext.getBean(MedicationResourceProvider.class), // #183
                applicationContext.getBean(LocationResourceProvider.class),
                applicationContext.getBean(AppointmentResourceProvider.class),
                //applicationContext.getBean(ScheduleResourceProvider.class), // #183
                applicationContext.getBean(SlotResourceProvider.class)
             
        ));

        CorsConfiguration config = new CorsConfiguration();
        config.setMaxAge(10L);
        config.addAllowedOrigin("*");
        config.setAllowCredentials(Boolean.TRUE);
        config.setExposedHeaders(Arrays.asList(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));
        config.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ACCEPT,
                HttpHeaders.ACCEPT_ENCODING,
                HttpHeaders.ACCEPT_LANGUAGE,
                HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CACHE_CONTROL,
                HttpHeaders.CONNECTION,
                HttpHeaders.CONTENT_LENGTH,
                SystemHeader.PREFER,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.COOKIE,
                HttpHeaders.HOST,
                HttpHeaders.ORIGIN,
                HttpHeaders.PRAGMA,
                HttpHeaders.REFERER,
                SystemHeader.SSP_FROM,
                SystemHeader.SSP_INTERACTIONID,
                SystemHeader.SSP_TO,
                SystemHeader.SSP_TRACEID,
                HttpHeaders.USER_AGENT,
                SystemHeader.X_REQUESTED_WITH));

        registerInterceptor(new CorsInterceptor(config));
        registerInterceptor(applicationContext.getBean(FhirRequestAuthInterceptor.class));
        registerInterceptor(applicationContext.getBean(FhirRequestGenericIntercepter.class));
        registerInterceptor(applicationContext.getBean(PatientJwtValidator.class));
        
        GpConnectServerCapabilityStatementProvider capStatementProvider = new GpConnectServerCapabilityStatementProvider(this);
        super.setServerConformanceProvider(capStatementProvider);
    }
}
