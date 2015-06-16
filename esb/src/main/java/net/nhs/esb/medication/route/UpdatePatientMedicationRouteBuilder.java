package net.nhs.esb.medication.route;

import net.nhs.esb.medication.model.MedicationUpdate;
import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class UpdatePatientMedicationRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.medicationTemplate}")
    private String medicationTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:updatePatientMedicationComposition").routeId("openEhrUpdatePatientMedicationComposition")
                .setHeader("compositionId", simple("${body.compositionId}"))
                .convertBodyTo(MedicationUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientMedicationComposition");

        from("direct:openEhrUpdatePatientMedicationComposition")
                
                .setHeader("Camel.openEHR.composition", simple("${body.content}"))
                .removeHeaders("composition")
                
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                
                .setHeader("composition", simple("${header.Camel.openEHR.composition}"))
                .removeHeaders("Camel.openEHR.composition")
                
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
                .setHeader("template", constant(medicationTemplate))
                .bean(compositionUpdateParameters)
                
                .removeHeaders("composition")
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
