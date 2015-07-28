package net.nhs.esb.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 */
public class DefaultAggregationStrategy<T> implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (oldExchange == null) {
            return convertBodyToList(newExchange);
        }

        List<T> compositionList = oldExchange.getIn().getBody(List.class);
        T composition = (T)newExchange.getIn().getBody();

        compositionList.add(composition);

        return oldExchange;
    }

    private Exchange convertBodyToList(Exchange exchange) {

        Message in = exchange.getIn();
        Object body = in.getBody();

        if (body instanceof List) {
            return exchange;
        }

        List<T> compositionList = new ArrayList<>();
        compositionList.add((T)body);

        in.setBody(compositionList);

        return exchange;
    }
}
