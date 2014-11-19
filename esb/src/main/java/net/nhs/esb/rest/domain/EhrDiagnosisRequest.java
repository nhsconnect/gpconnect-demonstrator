package net.nhs.esb.rest.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class EhrDiagnosisRequest {
	@JsonProperty("ctx/language")
	private String contextLanguage;
	@JsonProperty("ctx/territory")
	private String contextTerritory;
	private EhrDiagnosis diagnosis;
	
	public String getContextLanguage() {
		return contextLanguage;
	}
	
	public void setContextLanguage(String contextLanguage) {
		this.contextLanguage = contextLanguage;
	}

	public String getContextTerritory() {
		return contextTerritory;
	}

	public void setContextTerritory(String contextTerritory) {
		this.contextTerritory = contextTerritory;
	}

	public EhrDiagnosis getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(EhrDiagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}
}
