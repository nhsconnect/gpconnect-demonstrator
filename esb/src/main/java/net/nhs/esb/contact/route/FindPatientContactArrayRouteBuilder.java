package net.nhs.esb.contact.route;

import net.nhs.esb.contact.model.ContactArray;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientContactArrayRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:findPatientContactList").routeId("openEhrFindPatientContactList")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientContactList");

        from("direct:openEhrFindPatientContactList")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
                .setBody(simple(buildQuery()))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(ContactArray.class)
                .setBody(simple("${body.contacts}"))
                .end();
    }

    private String buildQuery() {
        return "select " +
                "a_a/data[at0001]/items[openEHR-EHR-CLUSTER.individual_professional_uk.v1]/items[openEHR-EHR-CLUSTER.person_name.v1]/items[at0001]/value/value as name, " +
                "a_a/data[at0001]/items[openEHR-EHR-CLUSTER.individual_professional_uk.v1]/items[openEHR-EHR-CLUSTER.telecom_uk.v1]/items[at0002]/value/value as contactInformation, " +
                "a_a/data[at0001]/items[at0035]/value/value as relationshipType, " +
                "a_a/data[at0001]/items[at0030]/value/value as relationship, " +
                "a_a/data[at0001]/items[at0025]/value/value as nextOfKin, " +
                "a_a/data[at0001]/items[at0017]/value/value as note " +
                "from EHR e[ehr_id/value='${header.ehrId}'] " +
                "contains COMPOSITION a " +
                "contains EVALUATION a_a[openEHR-EHR-ADMIN_ENTRY.relevant_contact_rcp.v1]";
    }
}
