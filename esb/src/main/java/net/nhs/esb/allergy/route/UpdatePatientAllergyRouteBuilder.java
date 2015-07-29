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
                .setHeader("Camel.compositionId", simple("${body.compositionId}"))
                .convertBodyTo(AllergyUpdate.class)
                .setHeader("Camel.composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientAllergyComposition");

        from("direct:openEhrUpdatePatientAllergyComposition")
                
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")

                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
                .setHeader("Camel.template", constant(allergiesTemplate))
                .bean(compositionUpdateParameters)
                
                .removeHeaders("Camel.*")
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
