package net.nhs.esb.endoflife.route;

import net.nhs.esb.endoflife.model.EndOfLifeUpdate;
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
public class CreateEndOfLifeRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.endOfLifeTemplate}")
    private String endOfLifeTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:createPatientEndOfLifeCarePlan").routeId("CreateEndOfLifeCarePlan")
            .convertBodyTo(EndOfLifeUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
            .setHeader("Camel.template", constant(endOfLifeTemplate))
            .bean(compositionCreateParameters)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
