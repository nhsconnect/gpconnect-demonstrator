package net.nhs.esb.cancermdt.model;

/**
 */
public class CancerMDTComposition {

    private String compositionId;
    private CancerMDT cancerMDT;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public CancerMDT getCancerMDT() {
        return cancerMDT;
    }

    public void setCancerMDT(CancerMDT cancerMDT) {
        this.cancerMDT = cancerMDT;
    }
}
