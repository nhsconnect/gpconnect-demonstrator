package net.nhs.esb.referrals.route;

import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import net.nhs.esb.referrals.model.ReferralUpdate;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class UpdatePatientReferralsRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.referralsTemplate}")
    private String referralsTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParams;


    @Override
    public void configure() throws Exception {
        // TODO - test
        //@formatter:off
        from("direct:updatePatientReferralsComposition").routeId("UpdatePatientReferralsComposition")
            .setHeader("Camel.compositionId", simple("${body.compositionId}"))
            .convertBodyTo(ReferralUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
            .setHeader("Camel.template", constant(referralsTemplate))
            .bean(compositionUpdateParams)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
