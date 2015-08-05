package net.nhs.esb.mdtreport.route;

import net.nhs.esb.mdtreport.model.MDTReportUpdate;
import net.nhs.esb.openehr.route.CompositionCreateParameters;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CreateMDTReportRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.mdtReportTemplate}")
    private String mdtReportTemplate;

    @Autowired
    private CompositionCreateParameters compositionCreateParameters;

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:createMDTReport").routeId("CreateMDTReports")
            .convertBodyTo(MDTReportUpdate.class)
            .setHeader("Camel.composition", simple("${body.content}"))
            .setBody(simple("${header.patientId}"))
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("createComposition"))
            .setHeader("Camel.template", constant(mdtReportTemplate))
            .bean(compositionCreateParameters)
            .to("cxfrs:bean:rsOpenEhr")
        .end();
        //@formatter:on
    }
}
