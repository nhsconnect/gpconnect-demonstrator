package uk.gov.hscic.common.helpers;

import org.hl7.fhir.dstu3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import uk.gov.hscic.SystemURL;
import uk.gov.hscic.translations.TranslationEntity;
import uk.gov.hscic.translations.TranslationRepository;

@Component
public class CodeableConceptBuilder {
	
	private Coding concept;
	private Extension description;
	private Coding translation;
	
	@Autowired
	private TranslationRepository translationRepository;

	public CodeableConceptBuilder() {
		
	}
	
	public CodeableConceptBuilder addConceptCode(String system, String code, String display) {
		if (!Strings.isNullOrEmpty(system) && !Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(display)) {
			this.concept = buildCoding(system, code, display);
		}
		return this;
	}
	
	public CodeableConceptBuilder addTranslation(String translationId) {
		TranslationEntity entity = null;
		if (!Strings.isNullOrEmpty(translationId)) {
			entity = translationRepository.getTranslationById(new Long(translationId));
		}
		if (entity != null) {
			this.translation = buildCoding(entity.getSystem(), entity.getCode(), entity.getDisplay());
			this.translation.setUserSelected(true);
		}
		return this;
	}
	
	public CodeableConceptBuilder addDescription(String code, String display) {
		if (!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(display)) {
			Extension description = new Extension(SystemURL.SD_EXT_SCT_DESC_ID);
			if (!Strings.isNullOrEmpty(code)) {
				Extension descriptionId = new Extension("descriptionId", new IdType(code));
				description.addExtension(descriptionId);
			}
			if (!Strings.isNullOrEmpty(display)) {
				Extension descriptionDisplay = new Extension("descriptionDisplay", new StringType(display));
				description.addExtension(descriptionDisplay);
			}
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
	
	public CodeableConceptBuilder clear() {
		this.concept = null;
		this.description = null;
		this.translation = null;
		
		return this;
	}
	
	
}
