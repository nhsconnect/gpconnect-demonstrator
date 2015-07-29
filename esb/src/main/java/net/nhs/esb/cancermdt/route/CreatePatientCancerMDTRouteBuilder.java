package net.nhs.esb.cancermdt.route;

import java.util.List;

import net.nhs.esb.cancermdt.model.CancerMDT;
import net.nhs.esb.cancermdt.model.CancerMDTUpdate;
import net.nhs.esb.openehr.route.CompositionCreateParameters;
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
public class CreatePatientCancerMDTRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.cancermdtTemplate}")
    private String cancerMDTTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientCancerMDTComposition").routeId("openEhrCreatePatientCancerMDTComposition")

                .setHeader("Camel.openEHR.CancerMDTsComposition", simple("${body.cancerMDT}"))
                .setHeader("Camel.openEHR.CancerMDTsCompositionSize", simple("${body.cancerMDT.size}"))
                                
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                
                
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
                .setHeader("Camel.composition", simple("${body.content}"))
                
                .choice()
                .when(header("Camel.compositionId").isNull())
                .to("direct:openEhrCreatePatientCancerMDTComposition")
                .endChoice();
        
        
        from("direct:openEhrCreatePatientCancerMDTComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("Camel.template", constant(cancerMDTTemplate))
                .bean(compositionCreateParameters)
                .removeHeaders("Camel.*")
                .to("cxfrs:bean:rsOpenEhr");
        
    }
}
