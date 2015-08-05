package net.nhs.esb.appointments.route;

import net.nhs.esb.appointments.model.AppointmentsUpdate;
import net.nhs.esb.openehr.route.CompositionCreateParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class CreatePatientAppointmentsRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.appointmentsTemplate}")
    private String appointmentsTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParams;


    @Override
    public void configure() throws Exception {
        //@formatter:off
        from("direct:createPatientAppointmentsComposition").routeId("CreatePatientAppointmentsComposition")
            .convertBodyTo(AppointmentsUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
            .setHeader("Camel.template", constant(appointmentsTemplate))
            .bean(compositionCreateParams)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
