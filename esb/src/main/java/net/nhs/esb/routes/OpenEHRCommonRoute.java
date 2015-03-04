package net.nhs.esb.routes;

import net.nhs.esb.enrichers.DiagnosesEnricher;
import net.nhs.esb.enrichers.OpenEhrPatientParams;
import net.nhs.esb.rest.domain.EhrDiagnosisRequest;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenEHRCommonRoute extends SpringRouteBuilder {

    @Value("${openehr.user}")
    private String openEhrUsername;

    @Value("${openehr.password}")
    private String openEhrPassword;

    @Autowired
    private OpenEhrPatientParams openEhrPatientParams;

    @Autowired
    private DiagnosesEnricher diagnosesEnricher;

    @Override
    public void configure() throws Exception {
        from("direct:setHeaders")
                .setHeader("Accept", constant("application/json"))
                .setHeader("Content-Type", constant("application/json"))
                .removeHeader("Accept-Encoding")
        ;

        from("direct:createSession")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("login"))
                .setBody(constant(new String[]{ openEhrUsername, openEhrPassword }))
                .to("cxfrs:bean:rsOpenEhr")
                .log("SessionId: ${body.sessionId}")
                .setHeader("Ehr-Session", simple("${body.sessionId}"))
        ;

        // TODO Take out the hard-coded ehrId value
        from("direct:getEhrId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("ehr"))
                .bean(openEhrPatientParams)
                .to("cxfrs:bean:rsOpenEhr")
                .log("EhrId: ${body.ehrId}")
                .setHeader("ehrId", constant("0da489ee-c0ae-4653-9074-57b7f63c9f16"))
//                .setHeader("ehrId", simple("${body.ehrId}"))
        ;

        from("direct:createEhrDiagnosisRequest")
                .bean(diagnosesEnricher)
                .convertBodyTo(EhrDiagnosisRequest.class)
                .setHeader("ehrDiagnosisRequest", body())
        ;
    }
}
