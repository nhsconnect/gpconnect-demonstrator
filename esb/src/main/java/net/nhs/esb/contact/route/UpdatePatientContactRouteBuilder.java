package net.nhs.esb.contact.route;

import net.nhs.esb.contact.model.ContactUpdate;
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
public class UpdatePatientContactRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.contactsTemplate}")
    private String contactsTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:updatePatientContactComposition").routeId("openEhrUpdatePatientContactComposition")
                .setHeader("compositionId", simple("${body.compositionId}"))
                .convertBodyTo(ContactUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientContactComposition");

        from("direct:openEhrUpdatePatientContactComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
                .setHeader("template", constant(contactsTemplate))
                .bean(compositionUpdateParameters)
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
