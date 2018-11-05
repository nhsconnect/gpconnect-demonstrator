package uk.gov.hscic.patient.structuredAllergyIntolerance;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.model.patient.StructuredAllergyIntolerance;

public class StructuredAllergyEntityToDetailTransformer implements Transformer<StructuredAllergyIntoleranceEntity, StructuredAllergyIntolerance> {

	@Override
	public StructuredAllergyIntolerance transform(StructuredAllergyIntoleranceEntity item) {
		StructuredAllergyIntolerance structuredAllergyIntolerance = new StructuredAllergyIntolerance();
		structuredAllergyIntolerance.setId(item.getId());
        structuredAllergyIntolerance.setGuid(item.getGuid());
		structuredAllergyIntolerance.setEndDate(item.getEndDate());
		structuredAllergyIntolerance.setEndReason(item.getEndReason());
		structuredAllergyIntolerance.setNote(item.getNote());
		structuredAllergyIntolerance.setReactionDescription(item.getReactionDescription());
		structuredAllergyIntolerance.setClinicalStatus(item.getClinicalStatus());
		structuredAllergyIntolerance.setVerificationStatus(item.getVerificationStatus());
		structuredAllergyIntolerance.setCategory(item.getCategory());
		structuredAllergyIntolerance.setPatientRef(item.getPatientRef());
		structuredAllergyIntolerance.setOnSetDateTime(item.getOnSetDateTime());
		structuredAllergyIntolerance.setAssertedDate(item.getAssertedDate());
		structuredAllergyIntolerance.setConceptCode(item.getConceptCode());
		structuredAllergyIntolerance.setConceptDisplay(item.getConceptDisplay());
		structuredAllergyIntolerance.setDescCode(item.getDescCode());
		structuredAllergyIntolerance.setDescDisplay(item.getDescDisplay());
		structuredAllergyIntolerance.setManifestationCoding(item.getManifestationCoding());
		structuredAllergyIntolerance.setManifestationDisplay(item.getManifestationDisplay());
		structuredAllergyIntolerance.setManifestationDescCoding(item.getManifestationDescCoding());
		structuredAllergyIntolerance.setManifestationDescDisplay(item.getManifestationDescDisplay());
		
		return structuredAllergyIntolerance;
	}

}
