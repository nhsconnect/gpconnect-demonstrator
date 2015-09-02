package org.rippleosi.common.service;

/**
 */
public abstract class AbstractQueryStrategy<T> implements QueryStrategy<T> {

    private final String patientId;

    protected AbstractQueryStrategy(String patientId) {
        this.patientId = patientId;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }
}
