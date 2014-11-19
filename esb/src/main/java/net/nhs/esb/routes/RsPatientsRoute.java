package net.nhs.esb.routes;

import static net.nhs.domain.openehr.constants.RouteConstants.*;
import static net.nhs.esb.util.LoggingConstants.*;
import net.nhs.domain.openehr.constants.HeaderConstants;
import net.nhs.domain.openehr.model.DataSourceId;
import net.nhs.esb.aggregators.ListAddListAggregator;
import net.nhs.esb.enrichers.DiagnosesIdHeaderEnricher;

import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.apache.cxf.helpers.HttpHeaderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RsPatientsRoute extends SpringRouteBuilder {
	
	@Value("${rsPatientsRoute}")
	private String rsPatientsRoute;
	
	@Value("${openEHRGetDiagnosesForPatientRoute}")
	private String openEHRGetDiagnosesForPatientRoute;
	@Value("${legacyGetDiagnosesForPatientRoute}")
	private String legacyGetDiagnosesForPatientRoute;
	
	@Value("${openEHRSaveDiagnosisForPatientRoute}")
	private String openEHRSaveDiagnosisForPatientRoute;
	
	@Value("${openEHRGetDiagnosisForPatientRoute}")
	private String openEHRGetDiagnosisForPatientRoute;
	@Value("${legacyGetDiagnosisForPatientRoute}")
	private String legacyGetDiagnosisForPatientRoute;
	
	@Value("${openEHRUpdateDiagnosisForPatientRoute}")
	private String openEHRUpdateDiagnosisForPatientRoute;
	
	@Autowired
    private ListAddListAggregator listAddListAggregator;
	
	@Autowired
    private DiagnosesIdHeaderEnricher diagnosesIdHeaderEnricher;
	
    @Override
    public void configure() throws Exception {
        from(rsPatientsRoute).routeId(RS_PATIENTS_ROUTE)
        	.log(LOG_START)
        	.recipientList(simple(DIRECT + HEADER(CxfConstants.OPERATION_NAME)))
        	.removeHeader(HttpHeaderHelper.CONTENT_LENGTH)
        	.log(LOG_END)
        	;
        
        from(DIRECT +"getDiagnosesByPatientId")
            .multicast(AggregationStrategies.bean(listAddListAggregator))
            	.to(openEHRGetDiagnosesForPatientRoute, legacyGetDiagnosesForPatientRoute)
        		.end()
            ;
        
        from(DIRECT +"saveDiagnosisByPatientId")
	        .to(openEHRSaveDiagnosisForPatientRoute)
	        ;
        
        from(DIRECT +"getDiagnosisByIdAndPatientId")
        	.bean(diagnosesIdHeaderEnricher)
        	.choice()
                .when(header(HeaderConstants.DATA_SOURCE_ID).isEqualTo(DataSourceId.OPENEHR))
                    .to(openEHRGetDiagnosisForPatientRoute)
                .when(header(HeaderConstants.DATA_SOURCE_ID).isEqualTo(DataSourceId.LEGACY))
                    .to(legacyGetDiagnosisForPatientRoute)
        	;
        
        from(DIRECT +"updateDiagnosisByIdAndPatientId")
	        .bean(diagnosesIdHeaderEnricher)
	    	.choice()
	            .when(header(HeaderConstants.DATA_SOURCE_ID).isEqualTo(DataSourceId.OPENEHR))
	                .to(openEHRUpdateDiagnosisForPatientRoute)
	            .when(header(HeaderConstants.DATA_SOURCE_ID).isEqualTo(DataSourceId.LEGACY))
	                .to("log:notImplemented?showAll=true")
	    	;
    }
}
