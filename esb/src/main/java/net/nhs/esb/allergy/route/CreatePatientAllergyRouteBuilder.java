package net.nhs.esb.allergy.route;

import net.nhs.esb.allergy.model.AllergyUpdate;
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
public class CreatePatientAllergyRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.allergiesTemplate}")
    private String allergiesTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientAllergyComposition").routeId("openEhrCreatePatientAllergyComposition")
                .convertBodyTo(AllergyUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrCreatePatientAllergyComposition");

        from("direct:openEhrCreatePatientAllergyComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("template", constant(allergiesTemplate))
                .bean(compositionCreateParameters)
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
