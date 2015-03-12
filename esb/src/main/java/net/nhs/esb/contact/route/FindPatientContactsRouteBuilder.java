package net.nhs.esb.contact.route;

import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.openehr.route.CompositionSearchParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientContactsRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    @Override
    public void configure() throws Exception {

        from("direct:findPatientContactComposition").routeId("openEhrFindPatientContactComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientContactCompositionId")
                .to("direct:openEhrFindPatientContactComposition")
                .end();

        from("direct:openEhrFindPatientContactComposition")
                .bean(compositionParameters)
                .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(ContactComposition.class);

        from("direct:openEhrFindPatientContactCompositionId")
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
                "where a/name/value='Relevant contacts'";
    }
}
