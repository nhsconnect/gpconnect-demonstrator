package net.nhs.esb.transfer.route;

import net.nhs.esb.openehr.route.CompositionUpdateParameters;
import net.nhs.esb.transfer.model.TransferOfCareUpdate;
import net.nhs.esb.transfer.transform.TransferOfCareTransformer;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class UpdatePatientTransferRouteBuilder extends SpringRouteBuilder {

    @Value("${openehr.transferTemplate}")
    private String transferTemplate;

    @Autowired
    private CompositionUpdateParameters compositionUpdateParameters;
    
    @Autowired
    private TransferOfCareTransformer transferOfCareTransformer;

    @Override
    public void configure() throws Exception {

        from("direct:updatePatientTransferComposition").routeId("openEhrUpdatePatientTransferComposition")
        		.bean(transferOfCareTransformer, "createTransferOfCareComposition")
                .setHeader("compositionId", simple("${body.compositionId}"))
                .convertBodyTo(TransferOfCareUpdate.class)
                .setHeader("composition", simple("${body.content}"))
                .to("direct:openEhrUpdatePatientTransferComposition");

        from("direct:openEhrUpdatePatientTransferComposition")
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .setExchangePattern(ExchangePattern.InOut)
                .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
                .setHeader(CxfConstants.OPERATION_NAME, constant("updateComposition"))
                .setHeader("template", constant(transferTemplate))
                .bean(compositionUpdateParameters)
                .to("cxfrs:bean:rsOpenEhr")
                .process(new MyProcessor())
                .end();
    }

    private static class MyProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {

            Message in = exchange.getIn();

            System.out.println("*********** " + in.getBody());

            // TODO
        }
    }
}
