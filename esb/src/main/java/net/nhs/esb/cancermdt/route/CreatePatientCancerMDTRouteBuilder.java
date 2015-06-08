package net.nhs.esb.cancermdt.route;

import net.nhs.esb.cancermdt.model.CancerMDTUpdate;
import net.nhs.esb.openehr.route.CompositionCreateParameters;
import net.nhs.esb.openehr.route.HttpStatusProcessor;
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

    @Value("${openehr.cancermdtTemplate")
    private String cancerMDTTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientCancerMDTComposition").routeId("openEhrCreatePatientCancerMDTComposition")
                .convertBodyTo(CancerMDTUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .setBody(simple("${header.patientId}"))
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrCreatePatientCancerMDTCompositionId")
                .choice()
                    .when(header("compositionId").isNull())
                        .setBody(simple("${header.composition}"))
                        .to("direct:openEhrCreatePatientCancerMDTComposition")
                    .otherwise()
                        .process(new HttpStatusProcessor())
                .endChoice()
                .end();

        from("direct:openEhrCreatePatientCancerMDTComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("template", constant(cancerMDTTemplate))
                .bean(compositionCreateParameters)
                .to("cxfrs:bean:rsOpenEhr");
    }
}
