package net.nhs.esb.lab.orders.route;

import net.nhs.esb.lab.orders.model.LabOrderUpdate;
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
public class CreateLabOrderRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.labOrderTemplate}")
    private String labOrderTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:createLabOrderComposition").routeId("CreateLabOrder")
            .convertBodyTo(LabOrderUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
            .setHeader("Camel.template", constant(labOrderTemplate))
            .bean(compositionCreateParameters)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
