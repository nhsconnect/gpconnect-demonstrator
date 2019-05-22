package uk.gov.hscic;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class OperationOutcomeFactory {

    private OperationOutcomeFactory() {
    }

    /**
     * overload with null diagnostics
     * @param exception
     * @param code
     * @param issueType
     * @return BaseServerResponseException
     */
    public static BaseServerResponseException buildOperationOutcomeException(BaseServerResponseException exception,
            String code, IssueType issueType) {

        return buildOperationOutcomeException(exception, code, issueType, null);
    }

    /**
     * 
     * @param exception carries message used for diagnostics
     * @param code
     * @param issueType
     * @param diagnostics may be null but will override exception.message if set
     * @return BaseServerResponseException
     */
    public static BaseServerResponseException buildOperationOutcomeException(BaseServerResponseException exception,
            String code, IssueType issueType, String diagnostics) {
        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding(SystemURL.VS_GPC_ERROR_WARNING_CODE, code, code);
        codeableConcept.addCoding(coding);
        
        OperationOutcome operationOutcome = new OperationOutcome();

        OperationOutcomeIssueComponent ooIssue = new OperationOutcomeIssueComponent();
        ooIssue.setSeverity(IssueSeverity.ERROR).setDetails(codeableConcept).setCode(issueType);

        if (diagnostics != null) {
            ooIssue.setDiagnostics(diagnostics);
        } else {
            // #248 move exception.getMessage() from text to diagnostics element
            ooIssue.setDiagnostics(exception.getMessage());
        }

        
        operationOutcome.addIssue(ooIssue);

        operationOutcome.getMeta().addProfile(SystemURL.SD_GPC_OPERATIONOUTCOME);

        exception.setOperationOutcome(operationOutcome);
        return exception;
    }
}
