package net.nhs.esb.allergy.route;

import net.nhs.esb.allergy.model.AllergyCreate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CreatePatientAllergyRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:createPatientAllergyComposition").routeId("openEhrCreatePatientAllergyComposition")
                .convertBodyTo(AllergyCreate.class)
                .to("direct:openEhrCreatePatientAllergyComposition");

        from("direct:openEhrCreatePatientAllergyComposition")
                .process(new MyProcessor())
                .to("direct:setHeaders")
                .to("direct:createSession")
                .to("direct:getEhrId")
                .end();
    }

    private class MyProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println("****************");
            System.out.println(exchange.getIn().getBody());
            System.out.println("****************");
        }
    }
}
