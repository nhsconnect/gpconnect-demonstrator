package org.rippleosi.common.service;

import java.util.List;

/**
 */
public abstract class AbstractListQueryStrategy<T> extends AbstractQueryStrategy<List<T>> {

    protected AbstractListQueryStrategy(String patientId) {
        super(patientId);
    }
}
