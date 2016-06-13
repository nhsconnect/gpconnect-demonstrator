package uk.gov.hscic.common.config;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationContextUtils;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.PatientResourceProvider;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

@Configuration
@WebServlet(urlPatterns={"/fhir/*"}, displayName="FHIR Server")
public class FhirRestfulServlet extends RestfulServer {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initialize() throws ServletException {
        List<IResourceProvider> resourceProviders = new ArrayList<>();
        
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());

        resourceProviders.add(organizationResourceProvider(applicationContext));
        resourceProviders.add(practitionerResourceProvider(applicationContext));
        resourceProviders.add(patientResourceProvider(applicationContext));

        setResourceProviders(resourceProviders);
    }

    @Bean(name = "organizationResourceProvider")
    public OrganizationResourceProvider organizationResourceProvider(ApplicationContext applicationContext) {
        return new OrganizationResourceProvider(applicationContext);
    }

    @Bean(name = "practitionerResourceProvider")
    public PractitionerResourceProvider practitionerResourceProvider(ApplicationContext applicationContext) {
        return new PractitionerResourceProvider(applicationContext);
    }

    @Bean(name = "patientResourceProvider")
    public PatientResourceProvider patientResourceProvider(ApplicationContext applicationContext) {
        return new PatientResourceProvider(applicationContext);
    }
}
