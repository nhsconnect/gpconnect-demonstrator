package net.nhs.esb.cancermdt.model;

import java.util.List;

/**
 */
public class CancerMDTComposition {

    private String compositionId;
    private List<CancerMDT> cancerMDT;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<CancerMDT> getCancerMDT() {
        return cancerMDT;
    }

    public void setCancerMDT(List<CancerMDT> cancerMDT) {
        this.cancerMDT = cancerMDT;
    }
}
