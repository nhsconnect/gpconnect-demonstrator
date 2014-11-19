package net.nhs.esb.routes;

import static net.nhs.domain.openehr.constants.RouteConstants.DIRECT;
import net.nhs.esb.enrichers.DiagnosesEnricher;
import net.nhs.esb.enrichers.OpenEhrPatientParams;
import net.nhs.esb.rest.domain.EhrDiagnosisRequest;

import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenEHRCommonRoute extends SpringRouteBuilder {    
    @Autowired
    private OpenEhrPatientParams openEhrPatientParams;
    
    @Autowired
    private DiagnosesEnricher diagnosesEnricher;
    
    @Override
    public void configure() throws Exception {        
        from(DIRECT +"setHeaders")
    		.setHeader("Accept", constant("application/json"))
        	.setHeader("Content-Type", constant("application/json"))
        	.removeHeader("Accept-Encoding")
    	;
        
        from(DIRECT +"createSession")
			.setExchangePattern(ExchangePattern.InOut)
        	.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(false))
        	.setHeader(CxfConstants.OPERATION_NAME, constant("login"))
        	.setBody(constant(new String[] {"handi", "RPEcC859"}))
        	.to("cxfrs:bean:rsOpenEhr")
        	
        	.log("SessionId: ${body.sessionId}")
        	.setHeader("Ehr-Session", simple("${body.sessionId}"))
		;
        
        from(DIRECT +"getEhrId")
			.setExchangePattern(ExchangePattern.InOut)
        	.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(false))
        	.setHeader(CxfConstants.OPERATION_NAME, constant("ehr"))
        	.bean(openEhrPatientParams)
        	.to("cxfrs:bean:rsOpenEhr")
        	
        	.log("EhrId: ${body.ehrId}")
        	.setHeader("ehrId", simple("${body.ehrId}"))
		;
        
        from(DIRECT +"createEhrDiagnosisRequest")	    	
	    	.bean(diagnosesEnricher)
        	.convertBodyTo(EhrDiagnosisRequest.class)
        	.setHeader("ehrDiagnosisRequest", body())
	        ;
    }
}
