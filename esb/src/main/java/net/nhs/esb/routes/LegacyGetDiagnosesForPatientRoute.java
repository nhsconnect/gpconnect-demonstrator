package net.nhs.esb.routes;

import net.nhs.domain.openehr.model.Diagnoses;
import net.nhs.esb.aggregators.ListAddElementAggregator;
import net.nhs.esb.rest.Patients;
import net.nhs.repo.legacy.repository.EventRepository;
import static net.nhs.domain.openehr.constants.RouteConstants.*;
import static net.nhs.esb.util.LoggingConstants.*;

import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LegacyGetDiagnosesForPatientRoute extends SpringRouteBuilder {

	@Value("${legacyGetDiagnosesForPatientRoute}")
	private String legacyGetDiagnosesForPatientRoute;
	
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private ListAddElementAggregator listAddElementAggregator;
    
    @Override
    public void configure() throws Exception {
        //errorHandler(deadLetterChannel(openEHRGetDiagnosesForPatientDLC).useOriginalMessage().maximumRedeliveries(0));

        from(legacyGetDiagnosesForPatientRoute).routeId(LEGACY_GET_DIAGNOSES_FOR_PATIENT_ROUTE)
        	.log(LOG_START)
            .bean(eventRepository, "findByPatientId(" + HEADER(Patients.PATIENT_ID_HEADER) + ")")
            .split(body(), AggregationStrategies.beanAllowNull(listAddElementAggregator, "aggregate"))
            	.convertBodyTo(Diagnoses.class)
            	.end()
            ;
    }
}
