package uk.gov.hscic.organization;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Parameters.Parameter;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import java.text.SimpleDateFormat;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.organization.model.OrganizationDetails;
import uk.gov.hscic.organization.search.OrganizationSearch;
import uk.gov.hscic.organization.search.OrganizationSearchFactory;

public class OrganizationResourceProvider implements IResourceProvider {

    ApplicationContext applicationContext;

    @Autowired
    GetScheduleOperation getScheduleOperation;

    public OrganizationResourceProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }

    @Read()
    public Organization getOrganizationById(@IdParam IdDt organizationId) {

        RepoSource sourceType = RepoSourceType.fromString(null);
        OrganizationSearch organizationSearch = applicationContext.getBean(OrganizationSearchFactory.class).select(sourceType);
        OrganizationDetails organizationDetails = organizationSearch.findOrganizationDetails(organizationId.getIdPart());

        if (organizationDetails == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverityEnum.ERROR).setDetails("No organization details found for organization ID: " + organizationId.getIdPart());
            throw new InternalErrorException("No organization details found for organization ID: " + organizationId.getIdPart(), operationalOutcome);
        }
        return organizaitonDetailsToOrganizationResourceConverter(organizationDetails);
    }

    @Search
    public List<Organization> getOrganizationsByODSCode(@RequiredParam(name = Organization.SP_IDENTIFIER) TokenParam organizationId) {

        RepoSource sourceType = RepoSourceType.fromString(null);
        OrganizationSearch organizationSearch = applicationContext.getBean(OrganizationSearchFactory.class).select(sourceType);
        ArrayList<Organization> organizations = new ArrayList();

        List<OrganizationDetails> organizationDetailsList = null;

        if (organizationId.getValue() != null) {
            organizationDetailsList = organizationSearch.findOrganizationDetailsByOrgODSCode(organizationId.getValue());
        }

        if (organizationDetailsList != null) {
            for (OrganizationDetails organizationDetails : organizationDetailsList) {
                organizations.add(organizaitonDetailsToOrganizationResourceConverter(organizationDetails));
            }
        }

        return organizations;
    }

    @Operation(name = "$gpc.getschedule")
    public Bundle getSchedule(@ResourceParam Parameters params) {
        Bundle bundle = new Bundle();
        bundle.setType(BundleTypeEnum.SEARCH_RESULTS);

        OperationOutcome operationOutcome = new OperationOutcome();

        // params
        List<Parameter> parameters = params.getParameter();

        String orgOdsCode = null;
        String siteOdsCode = null;
        String planningHorizonStart = null;
        String planningHorizonEnd = null;

        for (int p = 0; p < parameters.size(); p++) {
            Parameter parameter = parameters.get(p);
            switch (parameter.getName()) {
                case "odsOrganisationCode":
                    orgOdsCode = ((IdentifierDt) parameter.getValue()).getValue();
                    break;
                case "odsSiteCode":
                    siteOdsCode = ((IdentifierDt) parameter.getValue()).getValue();
                    break;
                case "timePeriod":
                    PeriodDt timePeriod = (PeriodDt) parameter.getValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                    planningHorizonStart = dateFormat.format(timePeriod.getStart());
                    planningHorizonEnd = dateFormat.format(timePeriod.getEnd());
                    break;
            }
        }

        if (orgOdsCode != null && planningHorizonStart != null && planningHorizonEnd != null) {
            getScheduleOperation.populateBundle(bundle, operationOutcome, orgOdsCode, siteOdsCode, planningHorizonStart, planningHorizonEnd);
        } else {
            String msg = String.format("Not all of the mandatory parameters were provided - orgOdsCode - %s siteOdsCode - %s planningHorizonStart - %s planningHorizonEnd - %s", orgOdsCode, siteOdsCode, planningHorizonStart, planningHorizonEnd);
            operationOutcome.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails(msg);

            Entry operationOutcomeEntry = new Entry();
            operationOutcomeEntry.setResource(operationOutcome);
            bundle.addEntry(operationOutcomeEntry);
        }

        return bundle;
    }

    public Organization organizaitonDetailsToOrganizationResourceConverter(OrganizationDetails organizationDetails) {
        Organization organization = new Organization();
        organization.setId(String.valueOf(organizationDetails.getId()));
        organization.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-organization-code", organizationDetails.getOrgCode()));
        organization.addIdentifier(new IdentifierDt("http://fhir.nhs.net/Id/ods-site-code", organizationDetails.getSiteCode()));
        organization.setName(organizationDetails.getOrgName());
        organization.getMeta().setLastUpdated(organizationDetails.getLastUpdated());
        organization.getMeta().setVersionId(String.valueOf(organizationDetails.getLastUpdated().getTime()));
        return organization;
    }

}
