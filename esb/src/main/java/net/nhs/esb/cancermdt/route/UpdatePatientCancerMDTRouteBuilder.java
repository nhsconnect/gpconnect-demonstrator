package net.nhs.esb.cancermdt.route;

import java.util.List;

import net.nhs.esb.cancermdt.model.CancerMDT;
import net.nhs.esb.cancermdt.model.CancerMDTUpdate;
import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
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
                
                .setHeader("Camel.openEHR.CancerMDTsComposition", simple("${body.cancerMDT}"))
                .setHeader("Camel.openEHR.CancerMDTsCompositionSize", simple("${body.cancerMDT.size}"))
                
                .loop(header("Camel.openEHR.CancerMDTsCompositionSize"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchng) throws Exception {
                        CancerMDT cancerMDT = (CancerMDT)exchng.getIn().getHeader("Camel.openEHR.CancerMDTsComposition", List.class).get((Integer)exchng.getProperty("CamelLoopIndex"));
                        exchng.getIn().setBody(cancerMDT);
                    }
                })
                .setHeader("Camel.compositionId", simple("${body.compositionId}"))
                .convertBodyTo(CancerMDTUpdate.class)
                
                // Store composition into a Camel* header rather than "composition" so it is not sent to openEHR as it is too big for openEHR to accept
                //.setHeader("composition", simple("${body.content}"))
                .setHeader("Camel.openEHR.composition", simple("${body.content}"))
                
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                
                // Switch composition back into "composition" header for standard processor
                .setHeader("Camel.composition", simple("${header.Camel.openEHR.composition}"))
                .removeHeaders("Camel.openEHR.composition")
                
                .to("direct:openEhrUpdatePatientCancerMDTComposition")
                .end();
        
        
        from("direct:openEhrUpdatePatientCancerMDTComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
                .setHeader("Camel.template", constant(cancerMDTTemplate))
                .bean(compositionUpdateParameters)
                // Now the composition header details are back in the body the header needs removing or the header will be two large for openEHR
                .removeHeaders("composition")
                .to("cxfrs:bean:rsOpenEhr");
    }
}
