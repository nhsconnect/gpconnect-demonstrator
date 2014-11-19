package net.nhs.esb.routes;

import net.nhs.esb.aggregators.DiagnosesAggregator;
import net.nhs.esb.enrichers.DiagnosesEnricher;
import net.nhs.esb.enrichers.FindDiagnosisIdEnricher;
import net.nhs.esb.enrichers.OpenEhrUpdateDiagnosisParams;
import net.nhs.esb.rest.Patients;
import static net.nhs.domain.openehr.constants.RouteConstants.*;
import static net.nhs.esb.util.LoggingConstants.*;

import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenEHRUpdateDiagnosisForPatientRoute extends SpringRouteBuilder {

	@Value("${openEHRUpdateDiagnosisForPatientRoute}")
	private String openEHRUpdateDiagnosisForPatientRoute;
    
    @Autowired
    private DiagnosesAggregator diagnosesAggregator;
    
    @Autowired
    private DiagnosesEnricher diagnosesEnricher;
    
    @Autowired
    private OpenEhrUpdateDiagnosisParams openEhrUpdateDiagnosisParams;
    
    @Autowired
    private FindDiagnosisIdEnricher findDiagnosisIdEnricher;
    
    @Override
    public void configure() throws Exception {
        //errorHandler(deadLetterChannel(openEHRGetDiagnosesForPatientDLC).useOriginalMessage().maximumRedeliveries(0));
    	
        from(openEHRUpdateDiagnosisForPatientRoute).routeId(OPENEHR_UPDATE_DIAGNOSIS_FOR_PATIENT_ROUTE)
        	.log(LOG_START)
        	
        	.bean(diagnosesEnricher)
        	.setHeader("diagnosis", body())
        	
        	.to(DIRECT +"setHeaders")
        	.to(DIRECT +"createSession")
        	.to(DIRECT +"getEhrId")
        	
        	.setBody(header("diagnosis"))
        	.enrich(DIRECT +"enrichDiagnosisWithExisting", AggregationStrategies.bean(diagnosesAggregator))
        	
        	.to(DIRECT +"createEhrDiagnosisRequest")
        	.to(DIRECT +"updateDiagnosis")
        	.to(DIRECT +"getDiagnosis")
            ;
        
        from(DIRECT +"enrichDiagnosisWithExisting")
	    	.to("direct:getDiagnosis")
	        ;
		    
	    from(DIRECT +"updateDiagnosis")	    	
	    	.setExchangePattern(ExchangePattern.InOut)
	    	.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(false))
	    	.setHeader(CxfConstants.OPERATION_NAME, constant("update"))
	    	.bean(openEhrUpdateDiagnosisParams)
	    	.to("cxfrs:bean:rsOpenEhr")
	    	.bean(findDiagnosisIdEnricher)
	    	.setHeader(Patients.DIAGNOSIS_ID_HEADER, body())
	        ;
    }
}
