package uk.gov.hscic.medications;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.SystemURL;
import uk.gov.hscic.medication.requests.MedicationRequestResourceProvider;
import uk.gov.hscic.medication.statement.MedicationStatementEntityToDetailTransformer;
import uk.gov.hscic.medication.statement.MedicationStatementRepository;
import uk.gov.hscic.medication.statement.MedicationStatementResourceProvider;
import uk.gov.hscic.model.medication.MedicationStatementDetail;
import uk.gov.hscic.model.patient.PatientDetails;

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

	public Bundle addMedicationBundleEntries(Bundle structuredBundle, PatientDetails patientDetails) {
		BundleEntryComponent listEntry = new BundleEntryComponent();
		List<MedicationStatementDetail> medicationStatements = findMedicationStatements(Long.valueOf(patientDetails.getId())); 
		structuredBundle.addEntry(listEntry.setResource(createListEntry(medicationStatements, patientDetails.getNhsNumber())));
		medicationStatements.forEach(ms -> {
			createBundleEntries(ms).forEach(bundleEntry -> structuredBundle.addEntry(bundleEntry));
		});
		return structuredBundle;
	}
	
	private ListResource createListEntry(List<MedicationStatementDetail> medicationStatements, String nhsNumber) {
		ListResource medicationStatementsList = new ListResource();
		
		medicationStatementsList.setMeta(new Meta().addProfile(SystemURL.SD_GPC_MEDICATION_LIST));
		medicationStatementsList.setStatus(ListStatus.CURRENT);
		medicationStatementsList.setMode(ListMode.SNAPSHOT);
		medicationStatementsList.setTitle("Medication List");
		medicationStatementsList.setCode(new CodeableConcept().addCoding(new Coding(SystemURL.VS_SNOWMED, "TBD", "Medication List")));
		medicationStatementsList.setSubject(new Reference().setIdentifier(new Identifier().setValue(nhsNumber).setSystem(SystemURL.ID_NHS_NUMBER)));
		medicationStatementsList.setDate(new Date());
		medicationStatementsList.setOrderedBy(new CodeableConcept().addCoding(new Coding(SystemURL.CS_LIST_ORDER, "event-date", "Sorted by Event Date")));
		
		medicationStatements.forEach(statement -> {
			Reference statementRef = new Reference(new IdType("MedicationStatement", statement.getId()));
			ListEntryComponent listEntryComponent = new ListEntryComponent(statementRef);
			medicationStatementsList.addEntry(listEntryComponent);
		});
		
		return medicationStatementsList;
	}

	private List<BundleEntryComponent> createBundleEntries(MedicationStatementDetail statementDetail) {

		List<BundleEntryComponent> bundleEntryComponents = new ArrayList<>();
		
		bundleEntryComponents.add(new BundleEntryComponent()
				.setResource(medicationStatementResourceProvider.getMedicationStatementResource(statementDetail)));
		
		bundleEntryComponents.add(new BundleEntryComponent()
				.setResource(medicationResourceProvider.getMedicationResourceForBundle(statementDetail.getMedicationId())));
		
		MedicationRequest medicationRequest = medicationRequestResourceProvider
				.getMedicationRequestPlanResource(statementDetail.getMedicationRequestPlanId());
		bundleEntryComponents.add(new BundleEntryComponent().setResource(medicationRequest));
		
		medicationRequestResourceProvider.getMedicationRequestOrderResources(medicationRequest.getGroupIdentifier().getValue())
			.forEach(requestOrder -> bundleEntryComponents.add(new BundleEntryComponent().setResource(requestOrder)));
		
		return bundleEntryComponents;
		
	}

	private List<MedicationStatementDetail> findMedicationStatements(Long patientId) {
		List<MedicationStatementDetail> medicationStatements = new ArrayList<>();
		
		medicationStatementRepository.findByPatientId(patientId).forEach(entity ->{
			medicationStatements.add(medicationStatementEntityToDetailTransformer.transform(entity));
		});
		
		medicationStatements.sort(Comparator.comparing(medicationStatement -> medicationStatement.getDateAsserted()));
		
		return medicationStatements;
	}

}
