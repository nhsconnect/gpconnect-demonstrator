package net.nhs.esb.patient.route;

import net.nhs.esb.patient.model.PatientDetails;
import net.nhs.esb.patient.model.PatientDetailsArray;
import net.nhs.esb.patient.model.PatientSearchCriteria;
import net.nhs.esb.patient.transform.GetPatientDetailsSoapMessageTransformer;
import net.nhs.esb.patient.transform.PatientSearchTransformer;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class PatientSearchRouteBuilder extends SpringRouteBuilder {

    @Value("${smsp.address}")
    private String smspAddress;

    @Override
    public void configure() throws Exception {

        GetPatientDetailsSoapMessageTransformer messageTransformer = patientDetailsSoapMessageTransformer();
        PatientSearchTransformer patientSearchTransformer = patientSearchTransformer();

        from("cxfrs:bean:rsPatientSearch").routeId("patientSearchRoute")
                .log(simple("direct:${header." + CxfConstants.OPERATION_NAME + "}").toString())
                .recipientList(simple("direct:${header." + CxfConstants.OPERATION_NAME + "}"))
                .end();

        from("direct:getPatientDetailsBySearch")
                .convertBodyTo(PatientSearchCriteria.class)
                .bean(messageTransformer, "createRequest")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_URI, simple(smspAddress))
                .setHeader(Exchange.HTTP_PATH, constant("/getpatientdetailsbysearch"))
                .to(smspAddress)
                .to("direct:smspGetPatientDetailsBySearchResponse");

        from("direct:smspGetPatientDetailsBySearchResponse")
                .convertBodyTo(String.class)
                .convertBodyTo(PatientDetailsArray.class)
                .setBody(simple("${body.patientDetails}"))
                .end();

        from("direct:findAllPatients")
                .bean(patientSearchTransformer, "findAllPatients")
                .end();

        from("direct:findPatientById")
                .bean(patientSearchTransformer, "findPatientById")
                .end();

        from("direct:createPatient")
                .convertBodyTo(PatientDetails.class)
                .bean(patientSearchTransformer, "createPatient")
                .end();
    }

    private GetPatientDetailsSoapMessageTransformer patientDetailsSoapMessageTransformer() {
        return getApplicationContext().getBean(GetPatientDetailsSoapMessageTransformer.class);
    }

    private PatientSearchTransformer patientSearchTransformer() {
        return getApplicationContext().getBean(PatientSearchTransformer.class);
    }
}
