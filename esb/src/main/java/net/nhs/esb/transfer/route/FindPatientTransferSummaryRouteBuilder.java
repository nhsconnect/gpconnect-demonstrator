package net.nhs.esb.transfer.route;

import net.nhs.esb.transfer.route.converter.TransferSummaryAggregator;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientTransferSummaryRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private TransferSummaryAggregator transferSummaryAggregator;

    @Override
    public void configure() throws Exception {

        from("direct:findPatientTransferSummary").routeId("openEhrFindPatientTransferSummary")
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
                .to("direct:openEhrFindPatientTransferCompositionId")
                .bean(transferSummaryAggregator)
                .end();

    }
}
