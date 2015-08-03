package net.nhs.esb.referrals.route;

import net.nhs.esb.openehr.route.CompositionCreateParameters;
import net.nhs.esb.referrals.model.ReferralUpdate;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class CreatePatientReferralsRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.referralsTemplate}")
    private String referralsTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParams;

    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("direct:createPatientReferralsComposition").routeId("CreatePatientReferralsComposition")
            .convertBodyTo(ReferralUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
            .setHeader("Camel.template", constant(referralsTemplate))
            .bean(compositionCreateParams)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
