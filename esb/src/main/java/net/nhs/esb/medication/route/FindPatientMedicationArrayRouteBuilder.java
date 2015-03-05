package net.nhs.esb.medication.route;

import net.nhs.esb.medication.model.MedicationArray;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientMedicationArrayRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:findMedicationsByPatientId").routeId("findMedicationsByPatientId")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientMedicationList");

        from("direct:openEhrFindPatientMedicationList")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
                .setBody(simple(buildQuery()))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(MedicationArray.class)
                .setBody(simple("${body.medications}"))
                .end();
    }

    private String buildQuery() {
        return "select " +
                "a_a/items[openEHR-EHR-SECTION.current_medication_rcp.v1]/items[openEHR-EHR-EVALUATION.medication_statement_uk.v1]/data[at0001]/items[openEHR-EHR-CLUSTER.medication_item.v1]/items[at0001]/value/value as name, " +
                "a_a/items[openEHR-EHR-SECTION.current_medication_rcp.v1]/items[openEHR-EHR-EVALUATION.medication_statement_uk.v1]/data[at0001]/items[openEHR-EHR-CLUSTER.medication_item.v1]/items[at0002]/value/definingCode/code_string as route, " +
                "a_a/items[openEHR-EHR-SECTION.current_medication_rcp.v1]/items[openEHR-EHR-EVALUATION.medication_statement_uk.v1]/data[at0001]/items[openEHR-EHR-CLUSTER.medication_item.v1]/items[at0020]/value/value as doseAmount, " +
                "a_a/items[openEHR-EHR-SECTION.current_medication_rcp.v1]/items[openEHR-EHR-EVALUATION.medication_statement_uk.v1]/data[at0001]/items[openEHR-EHR-CLUSTER.medication_item.v1]/items[at0021]/value/value as doseTiming, " +
                "a_a/items[openEHR-EHR-SECTION.current_medication_rcp.v1]/items[openEHR-EHR-EVALUATION.medication_statement_uk.v1]/data[at0001]/items[openEHR-EHR-CLUSTER.medication_item.v1]/items[at0046]/items[at0039]/value/value as startDateTime, " +
                "a_a/items[openEHR-EHR-SECTION.current_medication_rcp.v1]/items[openEHR-EHR-EVALUATION.medication_statement_uk.v1]/data[at0001]/items[openEHR-EHR-CLUSTER.medication_item.v1]/items[at0003]/value/value as doseDirections " +
                "from EHR e[ehr_id/value='${header.ehrId}'] " +
                "contains COMPOSITION a " +
                "contains EVALUATION a_a[openEHR-EHR-SECTION.medication_medical_devices_rcp.v1]";
    }
}
