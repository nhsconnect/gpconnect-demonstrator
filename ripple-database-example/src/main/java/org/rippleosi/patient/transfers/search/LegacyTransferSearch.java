package org.rippleosi.patient.transfers.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.patient.transfers.common.TransferEntityToDetailsTransformer;
import org.rippleosi.patient.transfers.common.TransferEntityToSummaryTransformer;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareEntity;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;
import org.rippleosi.patient.transfers.model.TransferOfCareSummaryEntity;
import org.rippleosi.patient.transfers.repo.TransferOfCareRepository;
import org.rippleosi.patient.transfers.repo.TransferOfCareSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LegacyTransferSearch implements TransferOfCareSearch {

    @Value("${legacy.datasource.priority:900}")
    private int priority;

    @Autowired
    private TransferOfCareRepository transferOfCareRepository;

    @Autowired
    private TransferOfCareSummaryRepository transferOfCareSummaryRepository;

    @Autowired
    private TransferEntityToSummaryTransformer transferEntityToSummaryTransformer;

    @Autowired
    private TransferEntityToDetailsTransformer transferEntityToDetailsTransformer;

    @Override
    public String getSource() {
        return "legacy";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public List<TransferOfCareSummary> findAllTransfers(String patientId) {
        List<TransferOfCareSummaryEntity> transfers = transferOfCareSummaryRepository.findAllByPatientId(patientId);
        return CollectionUtils.collect(transfers, transferEntityToSummaryTransformer, new ArrayList<>());
    }

    @Override
    public TransferOfCareDetails findTransferOfCare(String patientId, String transferId) {
        TransferOfCareEntity transfer = transferOfCareRepository.findBySourceId(transferId);
        return transferEntityToDetailsTransformer.transform(transfer);
    }
}
