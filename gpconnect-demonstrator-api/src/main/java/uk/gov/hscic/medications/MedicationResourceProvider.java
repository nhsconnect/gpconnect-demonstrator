package uk.gov.hscic.medications;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.tomcat.util.http.parser.MediaTypeCache;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Medication.MedicationPackageBatchComponent;
import org.hl7.fhir.dstu3.model.Medication.MedicationPackageComponent;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.medication.statement.MedicationStatementEntity;
import uk.gov.hscic.medication.statement.MedicationStatementRepository;
import uk.gov.hscic.model.medication.MedicationDetail;
import uk.gov.hscic.model.medication.MedicationStatementDetail;
import uk.gov.hscic.patient.allergies.AllergyEntity;
import uk.gov.hscic.patient.details.PatientRepository;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergyIntoleranceEntity;

@Component
public class MedicationResourceProvider implements IResourceProvider {

    @Autowired
    private MedicationRepository medicationRepository;
    
    @Autowired
    private MedicationEntityToDetailTransformer medicationEntityToDetailTransformer;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private MedicationStatementRepository medicationStatementRepository;

    @Override
    public Class<Medication> getResourceType() {
        return Medication.class;
    }

    @Read()
    public Medication getMedicationById(@IdParam IdType medicationId) {
        MedicationEntity medicationEntity = medicationRepository.findOne(medicationId.getIdPartAsLong());

        if (medicationEntity == null) {
            OperationOutcome operationalOutcome = new OperationOutcome();
            operationalOutcome.addIssue().setSeverity(IssueSeverity.ERROR).setDiagnostics("No medication details found for ID: " + medicationId.getIdPart());
            throw new InternalErrorException("No medication details found for ID: " + medicationId.getIdPart(), operationalOutcome);
        }

        Medication medication = new Medication();

        String resourceId = String.valueOf(medicationEntity.getId());
        String versionId = String.valueOf(new Date());
        String resourceType = medication.getResourceType().toString();

        IdType id = new IdType(resourceType, resourceId, versionId);

        medication.setId(id);
        medication.getMeta().setVersionId(versionId);
                
        return medication;
    }
    
    public Medication getMedicationResourceForBundle(Long medicationId) {
		MedicationDetail medicationDetail = medicationEntityToDetailTransformer
				.transform(medicationRepository.findOne(medicationId));
		
		Medication medication = new Medication();
		
		medication.setId(new IdType(medicationDetail.getId()));
		
		medication.setMeta(new Meta().addProfile(SystemURL.SD_GPC_MEDICATION)
				.setVersionId(String.valueOf(new Date()))); 
		
		Coding coding = new Coding(SystemURL.VS_SNOWMED, 
				medicationDetail.getCode(), medicationDetail.getDisplay());
		
		medication.setCode(new CodeableConcept().addCoding(coding).setText(medicationDetail.getText()));
		
		addSnowmedExtensions(medication, medicationDetail);
		
		MedicationPackageComponent packageComponent = new MedicationPackageComponent();
		MedicationPackageBatchComponent batchComponent = new MedicationPackageBatchComponent();
		batchComponent.setLotNumber(medicationDetail.getBatchNumber());
		batchComponent.setExpirationDate(medicationDetail.getExpiryDate());
		packageComponent.addBatch(batchComponent);
		
		medication.setPackage(packageComponent);
		
		return medication;
	}

	private void addSnowmedExtensions(Medication medication, MedicationDetail medicationDetail) {
		Extension idExtension = null, displayExtension = null;
		Extension snowmedExtension = new Extension(SystemURL.SD_EXT_SCT_DESC_ID);
		
		if(medicationDetail.getSnowmedDescriptionId() != null) 
			idExtension = new Extension("descriptionId").setValue(new IdType(medicationDetail.getSnowmedDescriptionId()));
			snowmedExtension.addExtension(idExtension);
		
		if(medicationDetail.getSnowmedDescriptionDisplay() != null)
			displayExtension = new Extension("descriptionId").setValue(new StringType(medicationDetail.getSnowmedDescriptionDisplay()));
			snowmedExtension.addExtension(displayExtension);
		
		if(idExtension != null || displayExtension != null)
			medication.addExtension(snowmedExtension);
	}

	public Map<String,List<String>> getAllMedicationAndAllergiesForPatient(String nhsNumber) {
		Map<String,List<String>> allergiesAssociatedWithMedicationMap = new HashMap<>();
		for (MedicationEntity medicationEntity:medicationRepository.findAll()) {
			List<StructuredAllergyIntoleranceEntity> allergyEntities = medicationEntity.getMedicationAllergies();
			if(allergyEntities.size() > 0) {
				for (StructuredAllergyIntoleranceEntity allergy :allergyEntities) {

					if(Objects.equals(allergy.getNhsNumber(), nhsNumber)) {
						allergiesAssociatedWithMedicationMap.computeIfAbsent(medicationEntity.getDisplay(), k-> new ArrayList<>()).add(allergy.getDisplay());
					} else {
						allergiesAssociatedWithMedicationMap.put(medicationEntity.getDisplay(), Collections.EMPTY_LIST);
					}


				}
			} else {
				allergiesAssociatedWithMedicationMap.put(medicationEntity.getDisplay(), Collections.EMPTY_LIST);
			}
		}
		return allergiesAssociatedWithMedicationMap;
	}

	public MedicationStatementEntity addMedicationStatement(LinkedHashMap data) throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		System.out.println(data);

		MedicationStatementEntity medicationStatementEntity = new MedicationStatementEntity();
		medicationStatementEntity.setDateAsserted(simpleDateFormat.parse("2017-06-12"));
		medicationStatementEntity.setDosageInstruction("Take with meals");
		medicationStatementEntity.setDosageText("Take 2 tablets every 4 hours");
		medicationStatementEntity.setEncounterId(8L);
		medicationStatementEntity.setEndDate(simpleDateFormat.parse("2018-06-12"));
		medicationStatementEntity.setLastIssueDate(simpleDateFormat.parse("2017-06-12"));
		medicationStatementEntity.setLastUpdated(simpleDateFormat.parse("2018-03-15"));
		medicationStatementEntity.setMedicationRequestId(1L);
		medicationStatementEntity.setMedicationId(medicationRepository.getMedicationIdByName((String) data.get("medication"))); // get from frontend
		medicationStatementEntity.setPatientId(patientRepository.getPatientIdByNhsNumbwer(String.valueOf(data.get("nhsNumber")))); // get from frontend

		return medicationStatementRepository.save(medicationStatementEntity);
	}
}
