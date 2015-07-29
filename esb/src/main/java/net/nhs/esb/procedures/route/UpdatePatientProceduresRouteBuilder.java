package net.nhs.esb.procedures.route;

import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import net.nhs.esb.procedures.model.ProcedureUpdate;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class UpdatePatientProceduresRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.proceduresTemplate}")
    private String proceduresTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:updatePatientProcedureComposition").routeId("UpdatePatientProcedures")
            .setHeader("Camel.compositionId", simple("${body.compositionId}"))
            .convertBodyTo(ProcedureUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
            .setHeader("Camel.template", constant(proceduresTemplate))
            .bean(compositionUpdateParameters)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
