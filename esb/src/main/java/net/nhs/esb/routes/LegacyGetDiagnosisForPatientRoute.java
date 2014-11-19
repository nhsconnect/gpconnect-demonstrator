package net.nhs.esb.routes;

import net.nhs.domain.openehr.constants.HeaderConstants;
import net.nhs.domain.openehr.model.Diagnoses;
import net.nhs.esb.rest.Patients;
import net.nhs.repo.legacy.repository.EventRepository;
import static net.nhs.domain.openehr.constants.RouteConstants.*;
import static net.nhs.esb.util.LoggingConstants.*;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LegacyGetDiagnosisForPatientRoute extends SpringRouteBuilder {

	@Value("${legacyGetDiagnosisForPatientRoute}")
	private String legacyGetDiagnosisForPatientRoute;
	
    @Autowired
    private EventRepository eventRepository;
    
    @Override
    public void configure() throws Exception {
        //errorHandler(deadLetterChannel(openEHRGetDiagnosesForPatientDLC).useOriginalMessage().maximumRedeliveries(0));

        from(legacyGetDiagnosisForPatientRoute).routeId(LEGACY_GET_DIAGNOSIS_FOR_PATIENT_ROUTE)
        	.log(LOG_START)
            .bean(eventRepository, "findByEventIdAndPatientId(" + HEADER(HeaderConstants.DIAGNOSIS_ENTITY_ID) + ", " + HEADER(Patients.PATIENT_ID_HEADER) + ")")
            .convertBodyTo(Diagnoses.class)
            ;
    }
}
