package uk.gov.hscic.common.config;

import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import uk.gov.hscic.common.filters.FhirRequestAuthInterceptor;
import uk.gov.hscic.common.filters.FhirRequestGenericIntercepter;
import uk.gov.hscic.metadata.GpConnectServerConformanceProvider;
import uk.gov.hscic.patient.PatientResourceProvider;

@Configuration
@WebServlet(urlPatterns = {"/fhir/*"}, displayName = "FHIR Server")
public class FhirRestfulServlet extends RestfulServer {
    private static final long serialVersionUID = 1L;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void initialize() throws ServletException {
        setResourceProviders(Arrays.asList(
                applicationContext.getBean(PatientResourceProvider.class)
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
                "Prefer",
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.COOKIE,
                HttpHeaders.HOST,
                HttpHeaders.ORIGIN,
                HttpHeaders.PRAGMA,
                HttpHeaders.REFERER,
                "Ssp-From",
                "Ssp-InteractionID",
                "Ssp-To",
                "Ssp-TraceID",
                HttpHeaders.USER_AGENT,
                "X-Requested-With"));

        registerInterceptor(new CorsInterceptor(config));
        registerInterceptor(applicationContext.getBean(FhirRequestAuthInterceptor.class));
        registerInterceptor(applicationContext.getBean(FhirRequestGenericIntercepter.class));
        
        GpConnectServerConformanceProvider conStatementProvider = new GpConnectServerConformanceProvider(this);
        super.setServerConformanceProvider(conStatementProvider);
    }
}
