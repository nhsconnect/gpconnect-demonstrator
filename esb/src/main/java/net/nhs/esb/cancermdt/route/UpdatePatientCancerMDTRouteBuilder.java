package net.nhs.esb.cancermdt.route;

import net.nhs.esb.cancermdt.model.CancerMDTUpdate;
import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class UpdatePatientCancerMDTRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.cancermdtTemplate}")
    private String cancerMDTTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:updatePatientCancerMDTComposition").routeId("openEhrUpdatePatientCancerMDTComposition")
                .setHeader("compositionId", simple("${body.compositionId}"))
                .convertBodyTo(CancerMDTUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientCancerMDTComposition");

        from("direct:openEhrUpdatePatientCancerMDTComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
                .setHeader("template", constant(cancerMDTTemplate))
                .bean(compositionUpdateParameters)
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    } 
}
