package net.nhs.esb.rest.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EhrValue {
	@JsonProperty("@class")
	private String atClazz;
	@JsonProperty("class")
	private String clazz;
	private String value;
	private List<String> other_reference_ranges;
	
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getOther_reference_ranges() {
		return other_reference_ranges;
	}

	public void setOther_reference_ranges(List<String> other_reference_ranges) {
		this.other_reference_ranges = other_reference_ranges;
	}
}
