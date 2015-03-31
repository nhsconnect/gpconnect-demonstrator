package net.nhs.esb.transfer.route;

import net.nhs.esb.openehr.route.CompositionCreateParameters;
import net.nhs.esb.openehr.route.HttpStatusProcessor;
import net.nhs.esb.transfer.model.TransferOfCareUpdate;
import net.nhs.esb.transfer.transform.TransferOfCareTransformer;

import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CreatePatientTransferRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.transferTemplate}")
    private String transferTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;
    
    @Autowired
    private TransferOfCareTransformer siteTransformer;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientTransferComposition").routeId("openEhrCreatePatientTransferComposition")
        		.bean(siteTransformer, "createTransferOfCareComposition")
                .convertBodyTo(TransferOfCareUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .setBody(simple("${header.patientId}"))
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientAllergyCompositionId")
                .choice()
                    .when(header("compositionId").isNull())
                        .setBody(simple("${header.composition}"))
                        .to("direct:openEhrCreatePatientTransferComposition")
                        
                    .otherwise()
                        .process(new HttpStatusProcessor())
                .endChoice()
                .end();

        from("direct:openEhrCreatePatientTransferComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("template", constant(transferTemplate))
                .bean(compositionCreateParameters)
                .to("cxfrs:bean:rsOpenEhr");
    }
}
