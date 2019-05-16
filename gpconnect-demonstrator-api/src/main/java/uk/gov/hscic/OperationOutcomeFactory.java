package uk.gov.hscic;

import java.util.Collections;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;

public class OperationOutcomeFactory {

    public static OperationOutcome buildOperationOutcome(String system, String code, String codableConceptText, String metaProfile, IssueTypeEnum issueTypeEnum) {
        CodingDt coding = new CodingDt().setDisplay(code).setSystem(system).setCode(code);
    	CodeableConceptDt errorCodableConcept = new CodeableConceptDt().setCoding(Collections.singletonList(coding));
        
        OperationOutcome operationOutcome = new OperationOutcome();
        // #248 move codableConceptText from text to diagnostics element
        operationOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setCode(issueTypeEnum).setDetails(errorCodableConcept).setDiagnostics(codableConceptText);
        operationOutcome.getMeta().addProfile(metaProfile);
        return operationOutcome;
    }
    
    
}
