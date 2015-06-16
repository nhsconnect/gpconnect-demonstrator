package net.nhs.esb.problem.route;

import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import net.nhs.esb.problem.model.ProblemUpdate;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class UpdatePatientProblemRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.problemTemplate}")
    private String problemTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:updatePatientProblemComposition").routeId("openEhrUpdatePatientProblemComposition")
                .setHeader("compositionId", simple("${body.compositionId}"))
                .convertBodyTo(ProblemUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientProblemComposition");

        from("direct:openEhrUpdatePatientProblemComposition")
                
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
                .setHeader("template", constant(problemTemplate))
                .bean(compositionUpdateParameters)
                
                .removeHeaders("composition")
                .to("cxfrs:bean:rsOpenEhr")
                .end();
    }
}
