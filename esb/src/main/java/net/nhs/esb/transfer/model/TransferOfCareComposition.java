package net.nhs.esb.transfer.model;

import java.util.List;

/**
 */
public class TransferOfCareComposition {

    private String compositionId;
    private List<TransferOfCare> transfers;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<TransferOfCare> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<TransferOfCare> transfers) {
        this.transfers = transfers;
    }
}
