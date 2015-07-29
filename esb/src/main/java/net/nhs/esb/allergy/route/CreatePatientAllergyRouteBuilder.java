package net.nhs.esb.allergy.route;

import net.nhs.esb.allergy.model.AllergyUpdate;
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
public class CreatePatientAllergyRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.allergiesTemplate}")
    private String allergiesTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientAllergyComposition").routeId("openEhrCreatePatientAllergyComposition")
                .convertBodyTo(AllergyUpdate.class)
                .setHeader("Camel.composition", simple("${body.content}"))
                .setBody(simple("${header.patientId}"))
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientAllergyCompositionId")
                .choice()
                    .when(header("Camel.compositionId").isNull())
                        .setBody(simple("${header.Camel.composition}"))
                        .to("direct:openEhrCreatePatientAllergyComposition")
                    .otherwise()
                        .process(new HttpStatusProcessor())
                .endChoice()
                .end();

        from("direct:openEhrCreatePatientAllergyComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("Camel.template", constant(allergiesTemplate))
                .bean(compositionCreateParameters)
                .removeHeaders("Camel.*")
                .to("cxfrs:bean:rsOpenEhr");
    }
}
