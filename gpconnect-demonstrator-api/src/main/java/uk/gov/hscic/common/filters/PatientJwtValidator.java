package uk.gov.hscic.common.filters;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.filters.model.WebToken;
import uk.gov.hscic.patient.PatientResourceProvider;
import uk.gov.hscic.util.NhsCodeValidator;

@Component
public class PatientJwtValidator extends AuthorizationInterceptor {
    @Autowired
    PatientResourceProvider patientResourceProvider;

    @Autowired
    WebTokenFactory webTokenFactory;

    @Value("${request.leeway:5}")
    private int futureRequestLeeway;

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails requestDetails) {
       validateNhsNumbers(webTokenFactory.getWebToken(requestDetails, futureRequestLeeway), requestDetails);

       return new RuleBuilder().allowAll().build();
    }

    public void validateNhsNumbers(WebToken webToken, RequestDetails requestDetails) {
        if("Patient".equals(requestDetails.getResourceName())) {

            String webTokenNhsNumber = webToken.getRequestedRecord().getIdentifierValue(SystemURL.ID_NHS_NUMBER);
            String requestNhsNumber = getNhsNumber(requestDetails);

            if(requestNhsNumber != null) {
                if(webTokenNhsNumber == null || webTokenNhsNumber.equals(requestNhsNumber) == false) {
                  throw OperationOutcomeFactory.buildOperationOutcomeException(
                  new InvalidRequestException("NHS number in identifier header doesn't match the header"),
                  SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
                }

                if (!NhsCodeValidator.nhsNumberValid(webTokenNhsNumber)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid NHS number: " + webTokenNhsNumber),
                            SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
                }
            }
            else {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("NHS number missing from request"),
                        SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
            }
        }
    }

    private String getNhsNumber(RequestDetails requestDetails) {
        ResourceBinding patientResourceBinding = getPatientResourceBinding(requestDetails.getServer());

        if (patientResourceBinding != null) {
            BaseMethodBinding<?> methodBinding = patientResourceBinding.getMethod(requestDetails);

            // the request may not be for the patient resource in which case
            // we would not expect a method binding
            Object parameterValue = null;

            if (methodBinding != null) {
                for (IParameter parameter : methodBinding.getParameters()) {
                    parameterValue = parameter.translateQueryParametersIntoServerArgument(requestDetails, methodBinding);

                    // the identifier may have been passed in the URL
                    if (parameterValue == null) {
                        parameterValue = MethodUtil.convertIdToType(requestDetails.getId(), IdDt.class);
                    }

                    if (parameterValue != null) {
                        String nhsNumber = patientResourceProvider.getNhsNumber(parameterValue);

                        if (null != nhsNumber) {
                            return nhsNumber;
                        }
                    }
                }
            }

            if (parameterValue != null) {
                if (RequestTypeEnum.GET == requestDetails.getRequestType()) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new ResourceNotFoundException("No patient details found for patient ID: " + parameterValue),
                            SystemCode.PATIENT_NOT_FOUND, IssueTypeEnum.INVALID_CONTENT);
                }

                // Otherwise, there should have been an identifier header
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("No NHS number submitted: " + parameterValue),
                        SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
            }
        }

        return null;
    }

    private ResourceBinding getPatientResourceBinding(IRestfulServerDefaults defaultServer) {
        ResourceBinding resourceBinding = null;

        if(defaultServer instanceof RestfulServer) {
            RestfulServer restfulServer = (RestfulServer) defaultServer;

            // if we get more than one Patient binding then return null
            resourceBinding = restfulServer.getResourceBindings().stream()
                                                                 .filter(currentResourceBinding -> "Patient".equalsIgnoreCase(currentResourceBinding.getResourceName()))
                                                                 .collect(Collectors.reducing((a, b) -> null))
                                                                 .orElse(null);

        }

        return resourceBinding;
    }
}