package uk.gov.hscic.medications;

import java.util.Date;

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
import uk.gov.hscic.model.medication.MedicationDetail;

@Component
public class MedicationResourceProvider implements IResourceProvider {

    @Autowired
    private MedicationRepository medicationRepository;
    
    @Autowired
    private MedicationEntityToDetailTransformer medicationEntityToDetailTransformer;

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
}
