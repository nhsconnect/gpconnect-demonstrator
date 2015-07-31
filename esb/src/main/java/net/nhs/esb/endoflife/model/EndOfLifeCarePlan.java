package net.nhs.esb.endoflife.model;

import java.util.List;

/**
 */
public class EndOfLifeCarePlan {

    private CareDocument careDocument;
    private List<PrioritiesOfCare> prioritiesOfCare;
    private TreatmentDecision treatmentDecision;
    private CprDecision cprDecision;
    private String source;

    public CareDocument getCareDocument() {
        return careDocument;
    }

    public void setCareDocument(CareDocument careDocument) {
        this.careDocument = careDocument;
    }

    public List<PrioritiesOfCare> getPrioritiesOfCare() {
        return prioritiesOfCare;
    }

    public void setPrioritiesOfCare(List<PrioritiesOfCare> prioritiesOfCare) {
        this.prioritiesOfCare = prioritiesOfCare;
    }

    public TreatmentDecision getTreatmentDecision() {
        return treatmentDecision;
    }

    public void setTreatmentDecision(TreatmentDecision treatmentDecision) {
        this.treatmentDecision = treatmentDecision;
    }

    public CprDecision getCprDecision() {
        return cprDecision;
    }

    public void setCprDecision(CprDecision cprDecision) {
        this.cprDecision = cprDecision;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
