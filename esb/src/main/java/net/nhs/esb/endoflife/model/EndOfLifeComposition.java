package net.nhs.esb.endoflife.model;

import java.util.List;

/**
 */
public class EndOfLifeComposition {

    private String compositionId;
    private List<EndOfLifeCarePlan> eolCarePlans;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<EndOfLifeCarePlan> getEolCarePlans() {
        return eolCarePlans;
    }

    public void setEolCarePlans(List<EndOfLifeCarePlan> eolCarePlans) {
        this.eolCarePlans = eolCarePlans;
    }
}
