package net.nhs.esb.openehr.route;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
*/
public class HttpStatusProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody(Response.notModified().build());
    }
}
