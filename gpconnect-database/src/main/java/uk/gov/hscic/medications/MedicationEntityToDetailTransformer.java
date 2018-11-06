package uk.gov.hscic.medications;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

import uk.gov.hscic.model.medication.MedicationDetail;

@Component
public class MedicationEntityToDetailTransformer implements Transformer<MedicationEntity, MedicationDetail> {

	@Override
	public MedicationDetail transform(MedicationEntity medicationEntity) {
		MedicationDetail medicationDetail = new MedicationDetail();
		
		medicationDetail.setId(medicationEntity.getId());
		medicationDetail.setConceptCode(medicationEntity.getConceptCode());
		medicationDetail.setConceptDisplay(medicationEntity.getConceptDisplay());
		medicationDetail.setDescCode(medicationEntity.getDescCode());
		medicationDetail.setDescDisplay(medicationEntity.getDescDisplay());
		medicationDetail.setCodeTranslationSystem(medicationEntity.getCodeTranslationSystem());
		medicationDetail.setCodeTranslationId(medicationEntity.getCodeTranslationId());
		medicationDetail.setCodeTranslationDisplay(medicationEntity.getCodeTranslationDisplay());
		medicationDetail.setText(medicationEntity.getText());
		medicationDetail.setBatchNumber(medicationEntity.getBatchNumber());
		medicationDetail.setExpiryDate(medicationEntity.getExpiryDate());
		medicationDetail.setLastUpdated(medicationEntity.getLastUpdated());
		
		return medicationDetail;
	}

	
}
