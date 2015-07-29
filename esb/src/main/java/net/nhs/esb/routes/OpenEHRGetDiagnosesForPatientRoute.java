package net.nhs.esb.routes;

import static net.nhs.domain.openehr.constants.RouteConstants.DIRECT;
import static net.nhs.domain.openehr.constants.RouteConstants.OPENEHR_GET_DIAGNOSES_FOR_PATIENT_ROUTE;
import static net.nhs.esb.util.LoggingConstants.LOG_START;

import net.nhs.domain.openehr.model.Diagnoses;
import net.nhs.esb.aggregators.ListAddElementAggregator;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenEHRGetDiagnosesForPatientRoute extends SpringRouteBuilder {

	@Value("${openEHRGetDiagnosesForPatientRoute}")
	private String openEHRGetDiagnosesForPatientRoute;
    
    @Autowired
    private ListAddElementAggregator listAddElementAggregator;
    
    @Override
    public void configure() throws Exception {
        //errorHandler(deadLetterChannel(openEHRGetDiagnosesForPatientDLC).useOriginalMessage().maximumRedeliveries(0));

        from(openEHRGetDiagnosesForPatientRoute).routeId(OPENEHR_GET_DIAGNOSES_FOR_PATIENT_ROUTE)
        	.log(LOG_START)
        	
        	.to(DIRECT +"setHeaders")
        	.to(DIRECT +"createSession")
        	.to(DIRECT +"getEhrId")
        	.to(DIRECT +"getDiagnoses")
            ;
        
        from(DIRECT +"getDiagnoses")	    	
	    	.setExchangePattern(ExchangePattern.InOut)
	    	.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(false))
	    	.setHeader(CxfConstants.OPERATION_NAME, constant("query"))
	    	.setBody(simple(
	    			"select "
	    			+ "a/uid/value as uid, "
	    			+ "a_a/data[at0001]/items[at0002]/value/value as problemDiagnosis, "
	    			+ "a_a/data[at0001]/items[at0009]/value/value as description, "
	    			+ "a_a/data[at0001]/items[at0003]/value/value as dateOfOnset, "
	    			+ "a_a/data[at0001]/items[at0005]/value/value as severity "
	    			+ "from EHR e [ehr_id/value ='${header.Camel.ehrId}'] "
	    			+ "contains COMPOSITION a "
	    			+ "contains EVALUATION a_a[openEHR-EHR-EVALUATION.problem_diagnosis.v1]"))
	    	.to("cxfrs:bean:rsOpenEhr")
	    	.setBody(simple("${body.resultSet}"))
	    	
	    	.split(body(), AggregationStrategies.beanAllowNull(listAddElementAggregator, "aggregate"))
	    		.convertBodyTo(Diagnoses.class)
	    		.end()
	        ;
    }
}
