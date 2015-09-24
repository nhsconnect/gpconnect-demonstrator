/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
