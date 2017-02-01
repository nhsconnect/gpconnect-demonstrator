package uk.gov.hscic;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;

public class OperationOutcomeFactory {

    public static OperationOutcome buildOperationOutcome(String system, String code, String codableConceptText, String metaProfile, IssueTypeEnum issueTypeEnum) {
        CodeableConceptDt errorCodableConcept = new CodeableConceptDt(system, code).setText(codableConceptText);

        OperationOutcome operationOutcome = new OperationOutcome();
        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setCode(issueTypeEnum).setDetails(errorCodableConcept);
        operationOutcome.getMeta().addProfile(metaProfile);

        return operationOutcome;
    }
}