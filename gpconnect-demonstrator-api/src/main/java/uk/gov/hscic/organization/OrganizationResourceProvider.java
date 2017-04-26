package uk.gov.hscic.organization;

import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.model.organization.OrganizationDetails;

@Component
public class OrganizationResourceProvider implements IResourceProvider {

    @Autowired
    private GetScheduleOperation getScheduleOperation;

    @Autowired
    private OrganizationSearch organizationSearch;

    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }

    @Read()
    public Organization getOrganizationById(@IdParam IdDt organizationId) {
        String organizationIdString = organizationId.getIdPart();

        OrganizationDetails organizationDetails = organizationSearch.findOrganizationDetails(Long.parseLong(organizationIdString));

        if (organizationDetails == null) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException("No organization details found for organization ID: " + organizationIdString),
                    SystemCode.ORGANISATION_NOT_FOUND, IssueTypeEnum.INVALID_CONTENT);
        }

        return convertOrganizaitonDetailsListToOrganizationList(Collections.singletonList(organizationDetails)).get(0);
    }

    @Search
    public List<Organization> getOrganizationsByODSCode(@RequiredParam(name = Organization.SP_IDENTIFIER) TokenParam tokenParam) {
        if (StringUtils.isBlank(tokenParam.getSystem()) || StringUtils.isBlank(tokenParam.getValue())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Missing identifier token"),
                    SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
        }

        switch (tokenParam.getSystem()) {
            case SystemURL.ID_ODS_ORGANIZATION_CODE:
                return convertOrganizaitonDetailsListToOrganizationList(organizationSearch.findOrganizationDetailsByOrgODSCode(tokenParam.getValue()));

            case SystemURL.ID_ODS_SITE_CODE:
                return convertOrganizaitonDetailsListToOrganizationList(organizationSearch.findOrganizationDetailsBySiteODSCode(tokenParam.getValue()));

            default:
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid system code"),
                        SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
        }
    }

    @Operation(name = "$gpc.getschedule")
    public Bundle getSchedule(@IdParam IdDt organizationId, @ResourceParam Parameters params) {
        List<PeriodDt> timePeriods = params.getParameter()
                .stream()
                .filter(parameter -> "timePeriod".equals(parameter.getName()))
                .map(Parameter::getValue)
                .map(PeriodDt.class::cast)
                .collect(Collectors.toList());

        if (1 != timePeriods.size()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid timePeriod parameter"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (1 != params.getParameter().size()) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid parameter quantity"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        try {
            Bundle bundle = new Bundle().setType(BundleTypeEnum.SEARCH_RESULTS);

            String planningHorizonStart = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(timePeriods.get(0).getStart());
            String planningHorizonEnd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(timePeriods.get(0).getEnd());

            long daysBetween = ChronoUnit.DAYS.between(
                    timePeriods.get(0).getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    timePeriods.get(0).getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            if (daysBetween < 0L || daysBetween > 14L) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnprocessableEntityException("Invalid timePeriods, was " + daysBetween + " days between (max is 14)"),
                        SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
            }

            getScheduleOperation.populateBundle(bundle, new OperationOutcome(), organizationId, planningHorizonStart, planningHorizonEnd);

            return bundle;
        } catch (BaseServerResponseException baseServerResponseException) {
            throw baseServerResponseException; // Just rethrow it!
        } catch (Exception e) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid timePeriod parameter"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }

    private List<Organization> convertOrganizaitonDetailsListToOrganizationList(List<OrganizationDetails> organizationDetails) {
        Map<String, Organization> map = new HashMap<>();

        for (OrganizationDetails organizationDetail : organizationDetails) {
            if (map.containsKey(organizationDetail.getOrgCode())) {
                map.get(organizationDetail.getOrgCode()).addIdentifier(new IdentifierDt(SystemURL.ID_ODS_SITE_CODE, organizationDetail.getSiteCode()));
            } else {
                Organization organization = new Organization()
                        .setName(organizationDetail.getOrgName())
                        .addIdentifier(new IdentifierDt(SystemURL.ID_ODS_ORGANIZATION_CODE, organizationDetail.getOrgCode()))
                        .addIdentifier(new IdentifierDt(SystemURL.ID_ODS_SITE_CODE, organizationDetail.getSiteCode()));

                organization.setId(String.valueOf(organizationDetail.getId()));

                organization.getMeta()
                        .addProfile(SystemURL.SD_GPC_ORGANIZATION)
                        .setLastUpdated(organizationDetail.getLastUpdated())
                        .setVersionId(String.valueOf(organizationDetail.getLastUpdated().getTime()));

                map.put(organizationDetail.getOrgCode(), organization);
            }
        }

        return new ArrayList<>(map.values());
    }
}
