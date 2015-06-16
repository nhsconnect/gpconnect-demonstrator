package net.nhs.esb.allergy.route;

import net.nhs.esb.allergy.model.AllergyUpdate;
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
public class UpdatePatientAllergyRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.allergiesTemplate}")
    private String allergiesTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:updatePatientAllergyComposition").routeId("openEhrUpdatePatientAllergyComposition")
                .setHeader("compositionId", simple("${body.compositionId}"))
                .convertBodyTo(AllergyUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientAllergyComposition");

        from("direct:openEhrUpdatePatientAllergyComposition")
                
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
                .setHeader("template", constant(allergiesTemplate))
                .bean(compositionUpdateParameters)
                
                .removeHeaders("composition")
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
