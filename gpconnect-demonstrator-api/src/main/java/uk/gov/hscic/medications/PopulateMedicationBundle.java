package uk.gov.hscic.medications;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.ListResource;
import org.hl7.fhir.dstu3.model.ListResource.ListEntryComponent;
import org.hl7.fhir.dstu3.model.ListResource.ListMode;
import org.hl7.fhir.dstu3.model.ListResource.ListStatus;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
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
import uk.gov.hscic.patient.PatientResourceProvider;
import uk.gov.hscic.practitioner.PractitionerResourceProvider;

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
	private OrganizationResourceProvider organizationResourceProvider;
	
	@Autowired
	private PatientResourceProvider patientResourceProvider;

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
		
		medicationStatementsList.setMeta(new Meta().addProfile(SystemURL.SD_GPC_LIST)
				.setVersionId(String.valueOf(new Date().getTime())));
		medicationStatementsList.setStatus(ListStatus.CURRENT);
		medicationStatementsList.setMode(ListMode.SNAPSHOT);
		medicationStatementsList.setTitle(SystemConstants.MEDICATION_LIST);
		medicationStatementsList.setCode(new CodeableConcept().addCoding(new Coding(SystemURL.VS_SNOWMED, "TBD", "Medication List")));
		medicationStatementsList.setSubject(new Reference().setIdentifier(new Identifier().setValue(nhsNumber).setSystem(SystemURL.ID_NHS_NUMBER)));
		medicationStatementsList.setDate(new Date());
		medicationStatementsList.setOrderedBy(new CodeableConcept().addCoding(new Coding(SystemURL.CS_LIST_ORDER, "event-date", "Sorted by Event Date")));
		
		if(medicationStatements.isEmpty()) {
			//TODO set empty reason
			medicationStatementsList.setEmptyReason(new CodeableConcept().setText("no content"));
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
		
		Long patientId = Long.valueOf(patientDetails.getId());
		Long patientPractitionerId = patientDetails.getGpId();
		Long patientOrganizationId = Long.valueOf(patientDetails.getManagingOrganization());
		
		IIdType recorderPractitionerRef = medicationRequest.getRecorder().getReferenceElement();
		IIdType performerOrganizationRef = medicationRequest.getDispenseRequest().getPerformer().getReferenceElement();
		IIdType requesterOnBehalfOfOrganizationRef = medicationRequest.getRequester().getOnBehalfOf().getReferenceElement();
		IIdType requesterAgent = medicationRequest.getRequester().getAgent().getReferenceElement();
		
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
		if(requesterAgent != null) {
			Long requesterAgentId = requesterAgent.getIdPartAsLong();
			if(requesterAgent.getResourceType().equals("Practitioner")){
				practitionerIds.add(requesterAgentId);
			}
			if(requesterAgent.getResourceType().equals("Organization")) {
				organizationIds.add(requesterAgentId);
			}
			if(requesterAgent.getResourceType().equals("Patient") && requesterAgentId != patientId) {
				try {
					Patient patient = patientResourceProvider.getPatientById(new IdType(requesterAgentId));
					bundleEntryComponents.add(new BundleEntryComponent().setResource(patient));
				} catch (FHIRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		List<Annotation> notes = medicationRequest.getNote();
		notes.forEach(note -> {
			if(note.hasAuthorReference()) {
				try {
					practitionerIds.add(note.getAuthorReference().getReferenceElement().getIdPartAsLong());
				} catch (FHIRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		practitionerIds.stream()
					   .filter(id -> id != patientPractitionerId)
					   .forEach(id -> {
						   Practitioner practitioner = practitionerResourceProvider
									.getPractitionerById(new IdType(id));
							bundleEntryComponents.add(new BundleEntryComponent().setResource(practitioner));
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
			return date.compareTo(startDate) > 0 && date.compareTo(endDate) < 0;
		} else if (startDate != null && endDate == null) {
			return date.compareTo(startDate) > 0;
		} else if (startDate == null && endDate != null) {
			return date.compareTo(endDate) < 0;
		} else {
			return true;
		}
	}

}
