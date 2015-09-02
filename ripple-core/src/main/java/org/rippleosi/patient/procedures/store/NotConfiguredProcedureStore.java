package org.rippleosi.patient.procedures.store;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class NotConfiguredProcedureStore implements ProcedureStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(String patientId, ProcedureDetails procedure) {
        throw notImplemented();
    }

    @Override
    public void update(String patientId, ProcedureDetails procedure) {
        throw notImplemented();
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + ProcedureStore.class.getSimpleName() + " instance");
    }
}
