package net.nhs.esb.endoflife.model;

/**
 */
public class EndOfLifeCarePlan {

    private CareDocument careDocument;
    private PrioritiesOfCare prioritiesOfCare;
    private TreatmentDecision treatmentDecision;
    private CprDecision cprDecision;
    private String source;

    public CareDocument getCareDocument() {
        return careDocument;
    }

    public void setCareDocument(CareDocument careDocument) {
        this.careDocument = careDocument;
    }

    public PrioritiesOfCare getPrioritiesOfCare() {
        return prioritiesOfCare;
    }

    public void setPrioritiesOfCare(PrioritiesOfCare prioritiesOfCare) {
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
