package org.rippleosi.patient.transfers.common;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;
import org.rippleosi.patient.transfers.model.TransferOfCareSummaryEntity;
import org.springframework.stereotype.Component;

@Component
public class TransferEntityToSummaryTransformer implements Transformer<TransferOfCareSummaryEntity, TransferOfCareSummary> {

    @Override
    public TransferOfCareSummary transform(TransferOfCareSummaryEntity transferEntity) {
        TransferOfCareSummary transferSummary = new TransferOfCareSummary();

        transferSummary.setSourceId(transferEntity.getSourceId());
        transferSummary.setSiteFrom(transferEntity.getSiteFrom());
        transferSummary.setSiteTo(transferEntity.getSiteTo());
        transferSummary.setDateOfTransfer(transferEntity.getDateOfTransfer());
        transferSummary.setSource(transferEntity.getSource());

        return transferSummary;
    }
}
