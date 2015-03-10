package net.nhs.esb.medication.route;

import net.nhs.esb.medication.model.MedicationUpdate;
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
public class CreatePatientMedicationRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.medicationTemplate")
    private String medicationTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientMedicationComposition").routeId("openEhrCreatePatientMedicationComposition")
                .convertBodyTo(MedicationUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrCreatePatientMedicationComposition");

        from("direct:openEhrCreatePatientMedicationComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("template", constant(medicationTemplate))
                .bean(compositionCreateParameters)
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
