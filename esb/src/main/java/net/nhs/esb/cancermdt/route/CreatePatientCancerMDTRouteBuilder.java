package net.nhs.esb.cancermdt.route;

import net.nhs.esb.cancermdt.model.CancerMDTUpdate;
import net.nhs.esb.openehr.route.CompositionCreateParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CreatePatientCancerMDTRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.cancermdtTemplate}")
    private String cancerMDTTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientCancerMDTComposition").routeId("openEhrCreatePatientCancerMDTComposition")
                .convertBodyTo(CancerMDTUpdate.class)
                
                // Store composition into a Camel* header rather than "composition" so it is not sent to openEHR as it is too big for openEHR to accept
                //.setHeader("composition", simple("${body.content}"))
                .setHeader("Camel.openEHR.composition", simple("${body.content}"))
                
                .setBody(simple("${header.patientId}"))
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                
                // Save body so we can just update if a Composition exists
                .setHeader("Camel.openEHR.getEhrIDResponse", simple("${body}"))
                
                .to("direct:openEhrFindPatientCancerMDTCompositionId")

                // Switch composition back into "composition" header for standard processor
                .setHeader("composition", simple("${header.Camel.openEHR.composition}"))
                .removeHeaders("Camel.openEHR.composition")
                
                .choice()
                .when(header("compositionId").isNull())
                .setBody(simple("${header.composition}"))
                .to("direct:openEhrCreatePatientCancerMDTComposition")
                .otherwise()
                .setBody(simple("${header.Camel.openEHR.getEhrIDResponse}"))
                .removeHeaders("Camel.openEHR.getEhrIDResponse")
                //.to("direct:openEhrUpdatePatientCancerMDTComposition")
                .endChoice()
                .end();

        from("direct:openEhrCreatePatientCancerMDTComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("template", constant(cancerMDTTemplate))
                .bean(compositionCreateParameters)
                .removeHeaders("composition")
                //.to("cxfrs:bean:rsOpenEhr")
                ;
    }
}
