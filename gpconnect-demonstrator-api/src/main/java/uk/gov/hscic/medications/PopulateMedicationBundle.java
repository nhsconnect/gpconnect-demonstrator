package uk.gov.hscic.medications;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListMode;
import org.hl7.fhir.dstu3.model.ListResource.ListStatus;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hscic.SystemConstants;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.medication.requests.MedicationRequestResourceProvider;
import uk.gov.hscic.medication.statement.MedicationStatementEntity;
import uk.gov.hscic.medication.statement.MedicationStatementEntityToDetailTransformer;
import uk.gov.hscic.medication.statement.MedicationStatementRepository;
import uk.gov.hscic.medication.statement.MedicationStatementResourceProvider;
import uk.gov.hscic.model.medication.MedicationStatementDetail;
import uk.gov.hscic.model.patient.PatientDetails;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;
import uk.gov.hscic.practitioner.PractitionerRoleResourceProvider;

import java.util.*;

@Component
public class PopulateMedicationBundle {

	@Autowired
	private MedicationStatementRepository medicationStatementRepository;

	@Autowired
	private MedicationStatementEntityToDetailTransformer medicationStatementEntityToDetailTransformer;

	@Autowired
	private MedicationResourceProvider medicationResourceProvider;

	@Autowired
	private MedicationStatementResourceProvider medicationStatementResourceProvider;

	@Autowired
	private MedicationRequestResourceProvider medicationRequestResourceProvider;

    @Autowired
	private PractitionerResourceProvider practitionerResourceProvider;

    @Autowired
    private PractitionerRoleResourceProvider practitionerRoleResourceProvider;

	@Autowired
	private OrganizationResourceProvider organizationResourceProvider;


    public Bundle addMedicationBundleEntries(Bundle structuredBundle, PatientDetails patientDetails, Boolean includePrescriptionIssues,
			Period medicationPeriod) {
		BundleEntryComponent listEntry = new BundleEntryComponent();
        List<MedicationStatementDetail> medicationStatements = findMedicationStatements(Long.valueOf(patientDetails.getId()), medicationPeriod);
		structuredBundle.addEntry(listEntry.setResource(createListEntry(medicationStatements, patientDetails.getNhsNumber())));
		medicationStatements.forEach(medicationStatement -> {
			createBundleEntries(medicationStatement, includePrescriptionIssues, patientDetails)
				.forEach(bundleEntry -> structuredBundle.addEntry(bundleEntry));
		});
		return structuredBundle;
	}

    private ListResource createListEntry(List<MedicationStatementDetail> medicationStatements, String nhsNumber) {
		ListResource medicationStatementsList = new ListResource();

        medicationStatementsList.setId(new IdType(1));

        medicationStatementsList.setMeta(new Meta().addProfile(SystemURL.SD_GPC_LIST)
				.setVersionId(String.valueOf(new Date().getTime())).setLastUpdated(new Date()));
		medicationStatementsList.setStatus(ListStatus.CURRENT);
		medicationStatementsList.setId(new IdDt(1));

		medicationStatementsList.setMode(ListMode.SNAPSHOT);
		medicationStatementsList.setTitle(SystemConstants.MEDICATION_LIST);
		medicationStatementsList.setCode(new CodeableConcept().addCoding(new Coding(SystemURL.VS_SNOWMED, "933361000000108", "Medication List")));
		medicationStatementsList.setSubject(new Reference(new IdType("Patient", 1L)).setIdentifier(new Identifier().setValue(nhsNumber).setSystem(SystemURL.ID_NHS_NUMBER)));
		medicationStatementsList.setDate(new Date());
		medicationStatementsList.setOrderedBy(new CodeableConcept().addCoding(new Coding(SystemURL.CS_LIST_ORDER, "event-date", "Sorted by Event Date")));

        if(medicationStatements.isEmpty()) {
			medicationStatementsList.setEmptyReason(new CodeableConcept().setText(SystemConstants.NO_CONTENT));
            medicationStatementsList.setNote(Arrays.asList(new Annotation(new StringType(SystemConstants.INFORMATION_NOT_AVAILABLE))));
		}
		medicationStatements.forEach(statement -> {
			Reference statementRef = new Reference(new IdType("MedicationStatement", statement.getId()));
			ListEntryComponent listEntryComponent = new ListEntryComponent(statementRef);
			medicationStatementsList.addEntry(listEntryComponent);
		});

        return medicationStatementsList;
	}

	private List<BundleEntryComponent> createBundleEntries(MedicationStatementDetail statementDetail, Boolean includePrescriptionIssues, PatientDetails patientDetails) {

		List<BundleEntryComponent> bundleEntryComponents = new ArrayList<>();

        bundleEntryComponents.add(new BundleEntryComponent()
				.setResource(medicationStatementResourceProvider.getMedicationStatementResource(statementDetail)));

        bundleEntryComponents.add(new BundleEntryComponent()
				.setResource(medicationResourceProvider.getMedicationResourceForBundle(statementDetail.getMedicationId())));

        MedicationRequest medicationRequest = medicationRequestResourceProvider
				.getMedicationRequestPlanResource(statementDetail.getMedicationRequestPlanId());
		bundleEntryComponents.add(new BundleEntryComponent().setResource(medicationRequest));

        if(includePrescriptionIssues) {
			medicationRequestResourceProvider.getMedicationRequestOrderResources(medicationRequest.getGroupIdentifier().getValue())
			.forEach(requestOrder -> bundleEntryComponents.add(new BundleEntryComponent().setResource(requestOrder)));
		}

        bundleEntryComponents.addAll(getReferencedResources(medicationRequest, patientDetails));

        return bundleEntryComponents;

    }

	private List<BundleEntryComponent> getReferencedResources(MedicationRequest medicationRequest, PatientDetails patientDetails) {

        Long patientPractitionerId = patientDetails.getGpId();
		Long patientOrganizationId = Long.valueOf(patientDetails.getManagingOrganization());

        IIdType recorderPractitionerRef = medicationRequest.getRecorder().getReferenceElement();
		IIdType performerOrganizationRef = medicationRequest.getDispenseRequest().getPerformer().getReferenceElement();
		IIdType requesterOnBehalfOfOrganizationRef = medicationRequest.getRequester().getOnBehalfOf().getReferenceElement();

        List<BundleEntryComponent> bundleEntryComponents = new ArrayList<>();

        Set<Long> practitionerIds = new HashSet<>();
		Set<Long> organizationIds = new HashSet<>();

        if(recorderPractitionerRef != null) {
			practitionerIds.add(recorderPractitionerRef.getIdPartAsLong());
		}
		if(performerOrganizationRef != null) {
			organizationIds.add(performerOrganizationRef.getIdPartAsLong());
		}
		if(requesterOnBehalfOfOrganizationRef != null) {
			organizationIds.add(performerOrganizationRef.getIdPartAsLong());
		}
		List<Annotation> notes = medicationRequest.getNote();
		notes.forEach(note -> {
			if(note.hasAuthorReference()) {
				try {
					practitionerIds.add(note.getAuthorReference().getReferenceElement().getIdPartAsLong());
				} catch (FHIRException e) {
					throw new UnprocessableEntityException(e.getMessage());
				}
			}
		});

        practitionerIds.stream()
					   .filter(id -> id != patientPractitionerId)
					   .forEach(id -> {
						   Practitioner practitioner = practitionerResourceProvider
									.getPractitionerById(new IdType(id));

                           final List<PractitionerRole> practitionerRoleList = practitionerRoleResourceProvider
                                   .getPractitionerRoleByPracticionerId(new IdType(id));

                           bundleEntryComponents.add(new BundleEntryComponent().setResource(practitioner));

                           practitionerRoleList.forEach(role -> bundleEntryComponents.add(new BundleEntryComponent().setResource(role)));
					   });

        organizationIds.stream()
					   .filter(id -> id != patientOrganizationId)
					   .forEach(id -> {
							Organization organization = organizationResourceProvider
									.getOrganizationById(new IdType(id));
							bundleEntryComponents.add(new BundleEntryComponent().setResource(organization));
					   });

        return bundleEntryComponents;

    }

	private List<MedicationStatementDetail> findMedicationStatements(Long patientId, Period medicationPeriod) {
		List<MedicationStatementEntity> medicationStatementEntities = medicationStatementRepository.findByPatientId(patientId);
		List<MedicationStatementDetail> medicationStatements = new ArrayList<>();

        List<MedicationStatementEntity> statementsfilteredByDate = new ArrayList<>();
		if(medicationPeriod != null) {
			medicationStatementEntities.forEach(statement -> {
				if(statement.getLastIssueDate() != null) {
					if(dateIsWithinPeriod(statement.getLastIssueDate(), medicationPeriod)){
						statementsfilteredByDate.add(statement);
					}
				} else if(statement.getStartDate() != null) {
					if(dateIsWithinPeriod(statement.getStartDate(), medicationPeriod)) {
						statementsfilteredByDate.add(statement);
					}
				} else if(statement.getDateAsserted() != null) {
					if(dateIsWithinPeriod(statement.getDateAsserted(), medicationPeriod)) {
						statementsfilteredByDate.add(statement);
					}
				}
            });
			statementsfilteredByDate.forEach(entity ->{
				medicationStatements.add(medicationStatementEntityToDetailTransformer.transform(entity));
			});
		} else {
			medicationStatementEntities.forEach(entity ->{
				medicationStatements.add(medicationStatementEntityToDetailTransformer.transform(entity));
			});
		}

		medicationStatements.sort(Comparator.comparing(medicationStatement -> medicationStatement.getDateAsserted()));

        return medicationStatements;
	}

	private boolean dateIsWithinPeriod(Date date, Period period) {
		Date startDate = period.getStart();
		Date endDate = period.getEnd();

        if(startDate != null && endDate != null) {
			return !date.before(startDate) && !date.after(endDate);
		} else if (startDate != null) {
			return !date.before(startDate);
		} else if (endDate != null) {
			return !date.after(endDate);
		} else {
			return true;
		}
	}

}
