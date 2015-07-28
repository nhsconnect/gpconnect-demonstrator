package net.nhs.esb.procedures.route;

import java.util.ArrayList;
import java.util.List;

import net.nhs.esb.procedures.model.ProcedureComposition;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 */
public class ProcedureAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (oldExchange == null) {
            return convertBodyToList(newExchange);
        }

        List<ProcedureComposition> compositionList = oldExchange.getIn().getBody(List.class);
        ProcedureComposition composition = newExchange.getIn().getBody(ProcedureComposition.class);

        compositionList.add(composition);

        return oldExchange;
    }

    private Exchange convertBodyToList(Exchange exchange) {

        Message in = exchange.getIn();
        Object body = in.getBody();

        if (body instanceof ProcedureComposition) {
            List<ProcedureComposition> compositionList = new ArrayList<>();
            compositionList.add((ProcedureComposition)body);

            in.setBody(compositionList);
        }

        return exchange;
    }
}
