package net.nhs.esb.referrals.route;

import net.nhs.esb.openehr.route.CompositionSearchParameters;
import net.nhs.esb.referrals.model.Referral;
import net.nhs.esb.util.DefaultAggregationStrategy;
import net.nhs.esb.util.EmptyList;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FindPatientReferralsRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionSearchParams;

    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("direct:findAllPatientReferralCompositions").routeId("FindAllPatientReferralCompositions")
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
            .setBody(simple(buildQuery()))
            .to("cxfrs:bean:rsOpenEhr")
            .choice()
                .when(body().isNotNull())
                    .split(simple("${body.resultSet}"), new DefaultAggregationStrategy<Referral>())
                        .setHeader("Camel.compositionId", simple("${body[uid]}"))
                        .bean(compositionSearchParams)
                        .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                        .to("cxfrs:bean:rsOpenEhr")
                        .convertBodyTo(Referral.class)
                .end()
            .endChoice()
            .otherwise()
                .removeHeader("CamelHttpResponseCode")
                .setBody(new EmptyList())
        .end();
        //@formatter:on
    }

    private String buildQuery() {
        return "select a/uid/value as uid " +
            "from EHR e[ehr_id/value='${header.Camel.ehrId}'] " +
            "contains COMPOSITION a[openEHR-EHR-COMPOSITION.encounter.v1] " +
            "where a/name/value='Referral'";
    }
}
