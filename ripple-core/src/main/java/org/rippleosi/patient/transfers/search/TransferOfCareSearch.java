package org.rippleosi.patient.transfers.search;

import java.util.List;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;

public interface TransferOfCareSearch extends Repository {

    List<TransferOfCareSummary> findAllTransfers(String patientId);

    TransferOfCareDetails findTransferOfCare(String patientId, String transferId);
}
