package uk.gov.hscic.common.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import uk.gov.hscic.appointments.AppointmentResourceProvider;
import uk.gov.hscic.appointments.ScheduleResourceProvider;
import uk.gov.hscic.appointments.SlotResourceProvider;
import uk.gov.hscic.common.filters.FhirRequestAuthInterceptor;
import uk.gov.hscic.common.filters.FhirRequestGenericIntercepter;
import uk.gov.hscic.location.LocationResourceProvider;
import uk.gov.hscic.medications.MedicationAdministrationResourceProvider;
import uk.gov.hscic.medications.MedicationDispenseResourceProvider;
import uk.gov.hscic.medications.MedicationOrderResourceProvider;
import uk.gov.hscic.medications.MedicationResourceProvider;
import uk.gov.hscic.order.OrderResourceProvider;
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
        
        resourceProviders.add(applicationContext.getBean(PatientResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(OrganizationResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(PractitionerResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(MedicationResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(MedicationOrderResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(MedicationDispenseResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(MedicationAdministrationResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(LocationResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(AppointmentResourceProvider.class));
		resourceProviders.add(applicationContext.getBean(ScheduleResourceProvider.class));
		resourceProviders.add(applicationContext.getBean(SlotResourceProvider.class));
        resourceProviders.add(applicationContext.getBean(OrderResourceProvider.class));
		
		setResourceProviders(resourceProviders);
        
        registerInterceptor(applicationContext.getBean(FhirRequestAuthInterceptor.class));
        registerInterceptor(applicationContext.getBean(FhirRequestGenericIntercepter.class));
      
    }
}
