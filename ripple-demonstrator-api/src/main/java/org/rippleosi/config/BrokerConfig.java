package org.rippleosi.config;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
public class BrokerConfig extends CamelConfiguration {

    @Value("${queue.broker.port:61616}")
    private String queuePort;

    @Bean
    public BrokerService brokerService() throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("embedded");
        brokerService.addConnector("tcp://localhost:" + queuePort);

        brokerService.start();

        return brokerService;
    }

    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        ActiveMQComponent component = new ActiveMQComponent();
        component.setBrokerURL("vm://embedded?create=false");

        camelContext.addComponent("activemq", component);
    }
}
