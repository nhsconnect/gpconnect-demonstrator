package org.rippleosi.patient.careplans.model;

import java.io.Serializable;

/**
 */
public class CarePlanDetails implements Serializable {

    private String source;
    private String sourceId;
    private CareDocument careDocument;
    private CPRDecision cprDecision;
    private PrioritiesOfCare prioritiesOfCare;
    private TreatmentDecision treatmentDecision;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public CareDocument getCareDocument() {
        return careDocument;
    }

    public void setCareDocument(CareDocument careDocument) {
        this.careDocument = careDocument;
    }

    public CPRDecision getCprDecision() {
        return cprDecision;
    }

    public void setCprDecision(CPRDecision cprDecision) {
        this.cprDecision = cprDecision;
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
}
