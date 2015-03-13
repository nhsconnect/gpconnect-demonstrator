package net.nhs.esb.allergy.route;

import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.openehr.route.CompositionSearchParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientAllergiesRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    @Override
    public void configure() throws Exception {

        from("direct:findPatientAllergyComposition").routeId("openEhrFindPatientAllergyComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientAllergyCompositionId")
                .to("direct:openEhrFindPatientAllergyComposition")
                .end();

        from("direct:openEhrFindPatientAllergyComposition")
                .bean(compositionParameters)
                .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(AllergyComposition.class);

        from("direct:openEhrFindPatientAllergyCompositionId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
                .setBody(simple(buildQuery()))
                .to("cxfrs:bean:rsOpenEhr")
                .setHeader("compositionId", simple("${body.resultSet[0][uid]}"));
    }

    private String buildQuery() {
        return "select a/uid/value as uid " +
                "from EHR e[ehr_id/value='${header.ehrId}'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "where a/name/value='Allergies list'";
    }
}
