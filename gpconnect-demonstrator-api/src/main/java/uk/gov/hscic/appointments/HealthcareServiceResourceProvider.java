package uk.gov.hscic.appointments;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.HealthcareService;
import org.hl7.fhir.dstu3.model.Identifier;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import static uk.gov.hscic.SystemURL.CS_DOS_SERVICE;
import static uk.gov.hscic.SystemURL.SD_GPC_HEALTHCARE_SERVICE;
import uk.gov.hscic.appointment.healthcareservice.HealthcareServiceSearch;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwInvalidRequest400_InvalidParameterException;
import uk.gov.hscic.model.appointment.HealthcareServiceDetail;

@Component
public class HealthcareServiceResourceProvider implements IResourceProvider {

    @Autowired
    private HealthcareServiceSearch healthcareServiceSearch;

    @Override
    public Class<HealthcareService> getResourceType() {
        return HealthcareService.class;
    }

    @Read()
    public HealthcareService getHealthcareServiceById(@IdParam IdType serviceId) {
        HealthcareServiceDetail healthcareServiceDetail = healthcareServiceSearch.findHealthcareServiceByID(serviceId.getIdPart());
        if (healthcareServiceDetail == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No HealthcareService details found for ID: " + serviceId.getIdPart()),
                    SystemCode.REFERENCE_NOT_FOUND, IssueType.INCOMPLETE);
        }

        return healthcareServiceDetailToHealthcareServiceResourceConverter(healthcareServiceDetail);
    }

    public List<HealthcareService> getHealthcareServiceForServiceId(String identiifier) {

        ArrayList<HealthcareService> healthcareServices = new ArrayList<>();
        List<HealthcareServiceDetail> healthcareServiceDetails = healthcareServiceSearch.findHealthcareServiceForIdentifier(identiifier);
        if (healthcareServiceDetails != null && !healthcareServiceDetails.isEmpty()) {
            for (HealthcareServiceDetail healthcareServiceDetail : healthcareServiceDetails) {
                healthcareServices.add(healthcareServiceDetailToHealthcareServiceResourceConverter(healthcareServiceDetail));
            }
        }

        return healthcareServices;
    }

    /**
     * Convert detail to resource
     * @param HealthcareServiceDetail
     * @return HealthcareService
     */
    private HealthcareService healthcareServiceDetailToHealthcareServiceResourceConverter(HealthcareServiceDetail healthcareServiceDetail) {
        HealthcareService healthcareService = new HealthcareService();

        healthcareService.setId("" + healthcareServiceDetail.getId());
        healthcareService.getMeta().setVersionId("636064088100870233");
        healthcareService.getMeta().addProfile(SD_GPC_HEALTHCARE_SERVICE);

        List<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier()
                .setSystem(CS_DOS_SERVICE)
                .setValue(healthcareServiceDetail.getIdentifier());
        identifiers.add(identifier);
        healthcareService.setIdentifier(identifiers);

        healthcareService.setName(healthcareServiceDetail.getName());
        healthcareService.setProvidedBy(new Reference("Organization/" + healthcareServiceDetail.getOrganizationId()));
        return healthcareService;
    }

    @Search
    public List<HealthcareService> getHealthcareServiceForServiceId(@OptionalParam(name = "identifier") TokenParam tokenParam) {
        if (tokenParam != null && (StringUtils.isBlank(tokenParam.getSystem()) || StringUtils.isBlank(tokenParam.getValue()))) {
            throwInvalidRequest400_InvalidParameterException("Missing identifier token");
        }

        if (tokenParam != null) {
            if (tokenParam.getSystem().equals(SystemURL.CS_DOS_SERVICE)) {

                List<HealthcareService> healthcareServices = healthcareServiceSearch.
                        findHealthcareServiceForIdentifier(tokenParam.getValue()).stream()
                        .map(this::healthcareServiceDetailToHealthcareServiceResourceConverter).collect(Collectors.toList());

                if (healthcareServices.isEmpty()) {
                    return null;
                }
                return healthcareServices;
            } else {
                throwInvalidRequest400_InvalidParameterException("Invalid system code");
            }
        } else {
            List<HealthcareService> healthcareServices = healthcareServiceSearch.
                    findAllHealthcareServices().stream()
                    .map(this::healthcareServiceDetailToHealthcareServiceResourceConverter).collect(Collectors.toList());

            if (healthcareServices.isEmpty()) {
                return null;
            }
            return healthcareServices;
        }
        return null;
    }

}
