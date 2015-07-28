package net.nhs.esb.util;

import java.util.Collections;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 */
public class EmptyList implements Expression {

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        return (T)Collections.emptyList();
    }
}
