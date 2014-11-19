package net.nhs.esb.routes;

import net.nhs.esb.enrichers.FindDiagnosisIdEnricher;
import net.nhs.esb.enrichers.OpenEhrCreateDiagnosisParams;
import net.nhs.esb.rest.Patients;
import static net.nhs.domain.openehr.constants.RouteConstants.*;
import static net.nhs.esb.util.LoggingConstants.*;

import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenEHRSaveDiagnosisForPatientRoute extends SpringRouteBuilder {

	@Value("${openEHRSaveDiagnosisForPatientRoute}")
	private String openEHRSaveDiagnosisForPatientRoute;
    
    @Autowired
    private OpenEhrCreateDiagnosisParams openEhrCreateDiagnosisParams;
    
    @Value("${openEHRGetDiagnosisForPatientRoute}")
	private String openEHRGetDiagnosisForPatientRoute;
    
    @Autowired
    private FindDiagnosisIdEnricher findDiagnosisIdEnricher;
    
    @Override
    public void configure() throws Exception {
        //errorHandler(deadLetterChannel(openEHRGetDiagnosesForPatientDLC).useOriginalMessage().maximumRedeliveries(0));
    	
        from(openEHRSaveDiagnosisForPatientRoute).routeId(OPENEHR_SAVE_DIAGNOSIS_FOR_PATIENT_ROUTE)
        	.log(LOG_START)
        	
        	.to(DIRECT +"createEhrDiagnosisRequest")
        	.to(DIRECT +"setHeaders")
        	.to(DIRECT +"createSession")
        	.to(DIRECT +"getEhrId")
        	.to(DIRECT +"createDiagnosis")
        	.to(DIRECT +"getDiagnosis")
            ;
        
        from(DIRECT +"createDiagnosis")	    	
	    	.setExchangePattern(ExchangePattern.InOut)
	    	.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(false))
	    	.setHeader(CxfConstants.OPERATION_NAME, constant("create"))
	    	.bean(openEhrCreateDiagnosisParams)
	    	.to("cxfrs:bean:rsOpenEhr")
	    	.bean(findDiagnosisIdEnricher)
	    	.setHeader(Patients.DIAGNOSIS_ID_HEADER, body())
	        ;
    }
}
