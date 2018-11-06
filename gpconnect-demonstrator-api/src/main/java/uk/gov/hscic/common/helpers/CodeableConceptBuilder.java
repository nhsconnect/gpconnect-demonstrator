package uk.gov.hscic.common.helpers;

import org.hl7.fhir.dstu3.model.*;

import com.google.common.base.Strings;

import uk.gov.hscic.SystemURL;

public class CodeableConceptBuilder {
	
	private Coding concept;
	private Extension description;
	private Coding translation;

	public CodeableConceptBuilder() {
		
	}
	
	public CodeableConceptBuilder addConceptCode(String system, String code, String display) {
		if (!Strings.isNullOrEmpty(system) && !Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(display)) {
			this.concept = buildCoding(system, code, display);
		}
		return this;
	}
	
	public CodeableConceptBuilder addTranslation(String system, String code, String display) {
		if (!Strings.isNullOrEmpty(system) && !Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(display)) {
			this.translation = buildCoding(system, code, display);
			this.translation.setUserSelected(true);
		}
		return this;
	}
	
	public CodeableConceptBuilder addDescription(String code, String display) {
		if (!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(display)) {
			Extension description = new Extension(SystemURL.SD_EXT_SCT_DESC_ID);

			Extension descriptionId = new Extension("descriptionId", new IdType(code));
			Extension descriptionDisplay = new Extension("descriptionDisplay", new StringType(display));
			description.addExtension(descriptionId);
			description.addExtension(descriptionDisplay);

			this.description = description;
		}

        return this;
	}
	
	public CodeableConcept build() {
		if (concept != null && description != null) {
			concept.addExtension(description);
		}
		
		CodeableConcept code = new CodeableConcept();
		if (concept != null) {
			code.addCoding(concept);
		}
		if (translation != null) {
			code.addCoding(translation);
		}
		
		return code;
	}
	
	private Coding buildCoding(String system, String code, String display) {
		Coding coding = new Coding();
		coding.setCode(code);
		coding.setDisplay(display);
		coding.setSystem(system);
		
		return coding;
	}
	
	
}
