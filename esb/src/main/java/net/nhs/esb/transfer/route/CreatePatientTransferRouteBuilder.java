package net.nhs.esb.transfer.route;

import net.nhs.esb.openehr.route.CompositionCreateParameters;
import net.nhs.esb.transfer.model.TransferOfCareUpdate;
import net.nhs.esb.transfer.route.converter.TransferCompositionAggregator;
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
    private TransferCompositionAggregator transferCompositionAggregator;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        from("direct:createPatientTransferComposition").routeId("openEhrCreatePatientTransferComposition")
                .setHeader("transfer", simple("${body}"))
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientAllergyCompositionId")
                .to("direct:openEhrFindPatientAllergyComposition")
                .setHeader("allergies", simple("${body}"))
                .to("direct:openEhrFindPatientContactCompositionId")
                .to("direct:openEhrFindPatientContactComposition")
                .setHeader("contacts", simple("${body}"))
                .to("direct:openEhrFindPatientMedicationCompositionId")
                .to("direct:openEhrFindPatientMedicationComposition")
                .setHeader("medication", simple("${body}"))
                .to("direct:openEhrFindPatientProblemCompositionId")
                .to("direct:openEhrFindPatientProblemComposition")
                .setHeader("problems", simple("${body}"))
                .bean(transferCompositionAggregator)
                .convertBodyTo(TransferOfCareUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrCreatePatientTransferComposition")
                .end();

        from("direct:openEhrCreatePatientTransferComposition")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
//                .setBody(constant("LCR Handover Summary Report.v0"))
//                .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
                .setHeader("template", constant(transferTemplate))
                .bean(compositionCreateParameters)
                .to("cxfrs:bean:rsOpenEhr");
    }
}
