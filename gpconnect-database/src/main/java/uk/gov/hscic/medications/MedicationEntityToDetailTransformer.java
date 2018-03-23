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
		medicationDetail.setCode(medicationEntity.getCode());
		medicationDetail.setDisplay(medicationEntity.getDisplay());
		medicationDetail.setText(medicationEntity.getText());
		medicationDetail.setSnowmedDescriptionId(medicationEntity.getSnowmedDescriptionId());
		medicationDetail.setSnowmedDescriptionDisplay(medicationEntity.getSnowmedDescriptionDisplay());
		medicationDetail.setBatchNumber(medicationEntity.getBatchNumber());
		medicationDetail.setExpiryDate(medicationEntity.getExpiryDate());
		medicationDetail.setLastUpdated(medicationEntity.getLastUpdated());
		
		return medicationDetail;
	}

	
}
