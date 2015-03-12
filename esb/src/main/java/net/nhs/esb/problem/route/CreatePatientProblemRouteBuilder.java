package net.nhs.esb.problem.route;

import net.nhs.esb.openehr.route.CompositionCreateParameters;
import net.nhs.esb.openehr.route.HttpStatusProcessor;
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
public class CreatePatientProblemRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.problemTemplate}")
    private String problemTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientProblemComposition").routeId("openEhrCreatePatientProblemComposition")
                .convertBodyTo(ProblemUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .setBody(simple("${header.patientId}"))
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrCreatePatientProblemCompositionId")
                .choice()
                    .when(header("compositionId").isNull())
                        .setBody(simple("${header.composition}"))
                        .to("direct:openEhrCreatePatientProblemComposition")
                    .otherwise()
                        .process(new HttpStatusProcessor())
                .endChoice()
                .end();

        from("direct:openEhrCreatePatientProblemComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("template", constant(problemTemplate))
                .bean(compositionCreateParameters)
                .to("cxfrs:bean:rsOpenEhr");
    }
}
