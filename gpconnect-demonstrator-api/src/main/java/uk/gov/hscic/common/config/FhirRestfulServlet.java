package uk.gov.hscic.common.config;

import ca.uhn.fhir.rest.server.RestfulServer;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
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
@WebServlet(urlPatterns = {"/fhir/*"}, displayName = "FHIR Server")
public class FhirRestfulServlet extends RestfulServer {
    private static final long serialVersionUID = 1L;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void initialize() throws ServletException {
        setResourceProviders(Arrays.asList(
                applicationContext.getBean(PatientResourceProvider.class),
                applicationContext.getBean(OrganizationResourceProvider.class),
                applicationContext.getBean(PractitionerResourceProvider.class),
                applicationContext.getBean(MedicationResourceProvider.class),
                applicationContext.getBean(MedicationOrderResourceProvider.class),
                applicationContext.getBean(MedicationDispenseResourceProvider.class),
                applicationContext.getBean(MedicationAdministrationResourceProvider.class),
                applicationContext.getBean(LocationResourceProvider.class),
                applicationContext.getBean(AppointmentResourceProvider.class),
                applicationContext.getBean(ScheduleResourceProvider.class),
                applicationContext.getBean(SlotResourceProvider.class),
                applicationContext.getBean(OrderResourceProvider.class)
        ));

        registerInterceptor(applicationContext.getBean(FhirRequestAuthInterceptor.class));
        registerInterceptor(applicationContext.getBean(FhirRequestGenericIntercepter.class));
    }
}
