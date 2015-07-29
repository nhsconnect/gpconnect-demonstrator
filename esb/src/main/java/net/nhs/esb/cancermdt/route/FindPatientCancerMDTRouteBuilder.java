package net.nhs.esb.cancermdt.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nhs.esb.cancermdt.model.CancerMDT;
import net.nhs.esb.cancermdt.model.CancerMDTComposition;
import net.nhs.esb.cancermdt.model.CancerMDTsComposition;
import net.nhs.esb.openehr.route.CompositionSearchParameters;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindPatientCancerMDTRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    @Override
    public void configure() throws Exception {

        from("direct:findPatientCancerMDTComposition").routeId("openEhrFindPatientCancerMDTComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .to("direct:openEhrFindPatientCancerMDTCompositionId")
                .end();

        from("direct:openEhrFindPatientCancerMDTCompositionId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
                .setBody(simple(buildQuery()))
                .to("cxfrs:bean:rsOpenEhr")
                
                // Save list of composition IDs
                .setHeader("Camel.openEHR.compositionIDsSize", simple("${body.resultSet.size}"))
                .setHeader("Camel.openEHR.compositionIDs", simple("${body.resultSet}"))
                
                // Loop through and get all compositions
                .loop(header("Camel.openEHR.compositionIDsSize"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchng) throws Exception {
                        // Set the compositionID in the header
                        List<Map<String,Object>> resultSet = (List<Map<String,Object>>)exchng.getIn().getHeader("Camel.openEHR.compositionIDs");
                        exchng.getIn().setHeader("Camel.compositionId", resultSet.get((Integer)exchng.getProperty("CamelLoopIndex")).get("uid"));
                    }
                })
                // call get composition route which will save the  composition to the header before doing the next one
                .to("direct:openEhrFindPatientCancerMDTComposition")
                .end()
                
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchng) throws Exception {
                        // Set body to list of MDTs
                        List cancerMDTList = (List<CancerMDT>)exchng.getIn().getHeader("Camel.openEHR.CancerMDTList", List.class);
                        CancerMDTsComposition cancerMDTsComposition = new CancerMDTsComposition();
                        cancerMDTsComposition.setCancerMDT(cancerMDTList);
                        exchng.getIn().setBody(cancerMDTsComposition);
                    }
                });

        from("direct:openEhrFindPatientCancerMDTComposition")
                .bean(compositionParameters)
                .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                .to("cxfrs:bean:rsOpenEhr")
                .convertBodyTo(CancerMDTComposition.class)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchng) throws Exception {
                        //Add cancerMDTs to the Camel.openEHR.CancerMDTList in the header object
                        List cancerMDTList = (List<CancerMDT>)exchng.getIn().getHeader("Camel.openEHR.CancerMDTList", List.class);
                        if(cancerMDTList == null){
                            cancerMDTList = new ArrayList<CancerMDT>();
                        }
                        cancerMDTList.add(exchng.getIn().getBody(CancerMDTComposition.class).getCancerMDT());
                        exchng.getIn().setHeader("Camel.openEHR.CancerMDTList", cancerMDTList);
                    }
                });
    }

    private String buildQuery() {
        return "select a/uid/value as uid "
                + "from EHR e[ehr_id/value='${header.Camel.ehrId}'] "
                + "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report.v1] "
                + "where a/name/value='Cancer MDT Output Report'"
                + "order by a/context/start_time/value desc";
    }
}
