package net.nhs.esb.medication.route;

import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.openehr.route.CompositionSearchParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientMedicationsRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:findPatientMedicationComposition").routeId("openEhrFindPatientMedicationComposition")
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .to("direct:openEhrFindPatientMedicationCompositionId")
            .to("direct:openEhrFindPatientMedicationComposition")
        .end();

        from("direct:openEhrFindPatientMedicationComposition")
            .bean(compositionParameters)
            .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
            .to("cxfrs:bean:rsOpenEhr")
            .convertBodyTo(MedicationComposition.class);

        from("direct:openEhrFindPatientMedicationCompositionId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
            .setBody(simple(buildQuery()))
            .to("cxfrs:bean:rsOpenEhr")
            .setHeader("Camel.compositionId", simple("${body.resultSet[0][uid]}"));
        //@formatter:on
    }

    private String buildQuery() {
        return "select a/uid/value as uid " +
                "from EHR e[ehr_id/value='${header.Camel.ehrId}'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.care_summary.v0] " +
                "where a/name/value='Current medication list'";
    }
}
