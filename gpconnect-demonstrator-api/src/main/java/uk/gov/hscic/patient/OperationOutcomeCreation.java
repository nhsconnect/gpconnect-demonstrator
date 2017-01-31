package uk.gov.hscic.patient;

import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
@Component
public class OperationOutcomeCreation {
    
   
    OperationOutcome buildOperationOutcomeNotFound(String system, String code, String codableConceptText,
            String metaProfile) {
        OperationOutcome operationOutcome = new OperationOutcome();
        CodingDt errorCoding = new CodingDt().setSystem(system).setCode(code);
        CodeableConceptDt errorCodableConcept = new CodeableConceptDt().addCoding(errorCoding);
        errorCodableConcept.setText(codableConceptText);
        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setCode(IssueTypeEnum.NOT_FOUND)
                .setDetails(errorCodableConcept);
        operationOutcome.getMeta().addProfile(metaProfile);
        return operationOutcome;

    }
    

    OperationOutcome buildOperationOutcomeInvalidContent(String system, String code, String codableConceptText,
            String metaProfile) {
        OperationOutcome operationOutcome = new OperationOutcome();
        CodingDt errorCoding = new CodingDt().setSystem(system).setCode(code);
        CodeableConceptDt errorCodableConcept = new CodeableConceptDt().addCoding(errorCoding);
        errorCodableConcept.setText(codableConceptText);
        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setCode(IssueTypeEnum.INVALID_CONTENT)
                .setDetails(errorCodableConcept);
        operationOutcome.getMeta().addProfile(metaProfile);
        return operationOutcome;

    }

}
