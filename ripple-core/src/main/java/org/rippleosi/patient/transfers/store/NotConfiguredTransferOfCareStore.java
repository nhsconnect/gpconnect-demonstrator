package org.rippleosi.patient.transfers.store;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;

public class NotConfiguredTransferOfCareStore implements TransferOfCareStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(String patientId, TransferOfCareDetails transferOfCare) {
        throw ConfigurationException.unimplementedTransaction(TransferOfCareStore.class);
    }
}
