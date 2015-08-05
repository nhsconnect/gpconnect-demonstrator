package net.nhs.esb.appointments.route;

import net.nhs.esb.appointments.model.AppointmentsUpdate;
import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class UpdatePatientAppointmentsRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.appointmentsTemplate}")
    private String appointmentsTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParams;


    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("direct:updatePatientAppointmentsComposition").routeId("UpdatePatientAppointmentsComposition")
            .setHeader("Camel.compositionId", simple("${body.compositionId}"))
            .convertBodyTo(AppointmentsUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
            .setHeader("Camel.template", constant(appointmentsTemplate))
            .bean(compositionUpdateParams)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
