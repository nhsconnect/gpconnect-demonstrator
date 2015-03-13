package net.nhs.esb.problem.route;

import net.nhs.esb.openehr.route.CompositionSearchParameters;
import net.nhs.esb.problem.model.ProblemComposition;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientProblemsRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    @Override
    public void configure() throws Exception {

        from("direct:findPatientProblemComposition").routeId("openEhrFindPatientProblemComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientProblemCompositionId")
                .to("direct:openEhrFindPatientProblemComposition")
                .end();

        from("direct:openEhrFindPatientProblemComposition")
                .bean(compositionParameters)
                .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(ProblemComposition.class);

        from("direct:openEhrFindPatientProblemCompositionId")
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
                "where a/name/value='Problem list'";
    }
}
