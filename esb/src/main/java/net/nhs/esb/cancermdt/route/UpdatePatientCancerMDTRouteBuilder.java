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
                
                // Store composition into a Camel* header rather than "composition" so it is not sent to openEHR as it is too big for openEHR to accept
                //.setHeader("composition", simple("${body.content}"))
                .setHeader("Camel.openEHR.composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientCancerMDTComposition");

        from("direct:openEhrUpdatePatientCancerMDTComposition")
                
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                
                // Switch composition back into "composition" header for standard processor
                .setHeader("composition", simple("${header.Camel.openEHR.composition}"))
                .removeHeaders("Camel.openEHR.composition")
                
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
                .setHeader("template", constant(cancerMDTTemplate))
                .bean(compositionUpdateParameters)
                
                // Now the composition header details are back in the body the header needs removing or the header will be two large for openEHR
                .removeHeaders("composition")
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
