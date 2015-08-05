package net.nhs.esb.mdtreport.route;

import net.nhs.esb.mdtreport.model.MDTReportUpdate;
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
public class UpdateMDTReportRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.mdtReportTemplate}")
    private String mdtReportTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:updateMDTReport").routeId("UpdateMDTReports")
            .setHeader("Camel.compositionId", simple("${body.compositionId}"))
            .convertBodyTo(MDTReportUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
            .setHeader("Camel.template", constant(mdtReportTemplate))
            .bean(compositionUpdateParameters)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
