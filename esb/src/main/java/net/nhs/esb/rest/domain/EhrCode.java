package net.nhs.esb.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EhrCode {
	@JsonProperty("@class")
	private String atClazz;
	@JsonProperty("class")
	private String clazz;
	private EhrValue terminology_id;
	private String code_string;
	
	public String getAtClazz() {
		return atClazz;
	}
	
	public void setAtClazz(String atClazz) {
		this.atClazz = atClazz;
	}
	
	public String getClazz() {
		return clazz;
	}
	
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public EhrValue getTerminology_id() {
		return terminology_id;
	}

	public void setTerminology_id(EhrValue terminology_id) {
		this.terminology_id = terminology_id;
	}

	public String getCode_string() {
		return code_string;
	}

	public void setCode_string(String code_string) {
		this.code_string = code_string;
	}
}
