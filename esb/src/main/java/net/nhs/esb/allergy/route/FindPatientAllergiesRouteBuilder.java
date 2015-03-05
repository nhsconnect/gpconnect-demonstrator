package net.nhs.esb.allergy.route;

import net.nhs.esb.allergy.model.AllergyArray;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientAllergiesRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:findAllergiesByPatientId").routeId("findAllergiesByPatientId")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientAllergyList");

        from("direct:openEhrFindPatientAllergyList")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
                .setBody(simple(buildQuery()))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(AllergyArray.class)
                .setBody(simple("${body.allergies}"))
                .end();

    }

    private String buildQuery() {
        return "select " +
                "a_a/items[openEHR-EHR-EVALUATION.adverse_reaction_uk.v1]/data[at0001]/items[at0002]/value/value as cause, " +
                "a_a/items[openEHR-EHR-EVALUATION.adverse_reaction_uk.v1]/data[at0001]/items[at0025]/items[at0022]/value/value as reaction " +
                "from EHR e[ehr_id/value='${header.ehrId}'] " +
                "contains COMPOSITION a " +
                "contains EVALUATION a_a[openEHR-EHR-SECTION.allergies_adverse_reactions_rcp.v1]";
    }

}
