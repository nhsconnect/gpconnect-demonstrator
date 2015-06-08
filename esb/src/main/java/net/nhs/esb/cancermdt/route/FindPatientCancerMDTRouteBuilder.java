package net.nhs.esb.cancermdt.route;

import net.nhs.esb.cancermdt.model.CancerMDTComposition;
import net.nhs.esb.openehr.route.CompositionSearchParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientCancerMDTRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    @Override
    public void configure() throws Exception {

        from("direct:findPatientCancerMDTComposition").routeId("openEhrFindPatientCancerMDTComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientCancerMDTCompositionId")
                .to("direct:openEhrFindPatientCancerMDTComposition")
                .end();

        from("direct:openEhrFindPatientCancerMDTComposition")
                .bean(compositionParameters)
                .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(CancerMDTComposition.class);

        from("direct:openEhrFindPatientCancerMDTCompositionId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
                .setBody(simple(buildQuery()))
                .to("cxfrs:bean:rsOpenEhr")
                .setHeader("compositionId", simple("${body.resultSet[0][uid]}"));
    }

    private String buildQuery() {
        return "select a/uid/value as uid "
                + "from EHR e[ehr_id/value='${header.ehrId}'] "
                + "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report.v1] "
                + "where a/name/value='Cancer MDT Output Report'"
                + "order by a/context/start_time/value desc";
    }
}
