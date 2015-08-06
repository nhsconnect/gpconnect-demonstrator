package net.nhs.esb.util;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class DefaultAggregationStrategy<T> implements AggregationStrategy {

    private final Predicate<T> predicate;

    public DefaultAggregationStrategy() {
        this(TruePredicate.<T>truePredicate());
    }

    public DefaultAggregationStrategy(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (oldExchange == null) {
            return convertBodyToList(newExchange);
        }

        List<T> compositionList = oldExchange.getIn().getBody(List.class);
        T composition = (T)newExchange.getIn().getBody();

        if (predicate.evaluate(composition)) {
            compositionList.add(composition);
        }

        return oldExchange;
    }

    private Exchange convertBodyToList(Exchange exchange) {

        Message in = exchange.getIn();
        Object body = in.getBody();

        if (body instanceof List) {
            return exchange;
        }

        List<T> compositionList = new ArrayList<>();
        T composition = (T) body;

        if (predicate.evaluate(composition)) {
            compositionList.add(composition);
        }

        in.setBody(compositionList);

        return exchange;
    }
}
