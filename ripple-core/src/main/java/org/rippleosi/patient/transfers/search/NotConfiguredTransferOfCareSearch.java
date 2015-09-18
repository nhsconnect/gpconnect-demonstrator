package org.rippleosi.patient.transfers.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;

public class NotConfiguredTransferOfCareSearch implements TransferOfCareSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<TransferOfCareSummary> findAllTransfers(String patientId) {
        throw ConfigurationException.unimplementedTransaction(TransferOfCareSearch.class);
    }

    @Override
    public TransferOfCareDetails findTransferOfCare(String patientId, String transferId) {
        throw ConfigurationException.unimplementedTransaction(TransferOfCareSearch.class);
    }
}
