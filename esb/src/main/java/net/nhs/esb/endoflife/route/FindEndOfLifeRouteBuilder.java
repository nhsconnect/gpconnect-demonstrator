package net.nhs.esb.endoflife.route;

import java.util.Collections;

import net.nhs.esb.endoflife.model.CareDocument;
import net.nhs.esb.endoflife.model.CprDecision;
import net.nhs.esb.endoflife.model.EndOfLifeCarePlan;
import net.nhs.esb.endoflife.model.EndOfLifeComposition;
import net.nhs.esb.endoflife.model.PrioritiesOfCare;
import net.nhs.esb.endoflife.model.TreatmentDecision;
import net.nhs.esb.openehr.route.CompositionSearchParameters;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindEndOfLifeRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    private static final DummyData dummyData = new DummyData();

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:findPatientEndOfLifeCarePlan").routeId("FindPatientEndOfLifeCarePlan")
// TODO Remove lines below and restore commented out code
            .bean(dummyData, "dummyData")
/*
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
            .setBody(simple(buildQuery()))
            .to("cxfrs:bean:rsOpenEhr")
            .choice()
                .when(body().isNotNull())
                    .split(simple("${body.resultSet}"), new DefaultAggregationStrategy<EndOfLifeComposition>())
                        .setHeader("compositionId", simple("${body[uid]}"))
                        .bean(compositionParameters)
                        .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                        .to("cxfrs:bean:rsOpenEhr")
                        .convertBodyTo(EndOfLifeComposition.class)
                    .end()
                .endChoice()
                .otherwise()
                    .removeHeader("CamelHttpResponseCode")
                    .setBody(new EmptyList())
*/
        .end();
        //@formatter:on
    }

    private String buildQuery() {
        return "select a/uid/value as uid " +
                "from EHR e[ehr_id/value='${header.Camel.ehrId}'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_plan.v1] " +
                "where a/name/value='End of Life Patient Preferences'";
    }

    public static class DummyData {
        public EndOfLifeComposition dummyData() {

            CareDocument careDocument = new CareDocument();
            careDocument.setName("End of Life Care");
            careDocument.setType("Document");
            careDocument.setAuthor("Dr John Smith");
            careDocument.setDate("2015-01-01");

            CprDecision cprDecision = new CprDecision();
            cprDecision.setCprDecision("For attempted cardio-pulmonary resuscitation");
            cprDecision.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.");
            cprDecision.setDateOfDecision("2015-01-02");

            PrioritiesOfCare priorities = new PrioritiesOfCare();
            priorities.setPlaceOfDeath("Home");
            priorities.setPlaceOfCare("Leeds General");
            priorities.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.");

            TreatmentDecision treatmentDecision = new TreatmentDecision();
            treatmentDecision.setDecisionToRefuseTreatment("Advanced decision to refuse treatment signed");
            treatmentDecision.setDateOfDecision("2015-01-03");
            treatmentDecision.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.");

            EndOfLifeCarePlan carePlan = new EndOfLifeCarePlan();
            carePlan.setSource("openehr");
            carePlan.setCareDocument(careDocument);
            carePlan.setCprDecision(cprDecision);
            carePlan.setPrioritiesOfCare(priorities);
            carePlan.setTreatmentDecision(treatmentDecision);

            EndOfLifeComposition endOfLifeComposition = new EndOfLifeComposition();
            endOfLifeComposition.setCompositionId("dummy");
            endOfLifeComposition.setEolCarePlans(Collections.singletonList(carePlan));

            return endOfLifeComposition;
        }
    }
}
