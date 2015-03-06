package net.nhs.esb.rest.domain;

import javax.xml.bind.annotation.XmlRootElement;

import net.nhs.esb.openehr.model.Meta;


@XmlRootElement
public class EhrDiagnosisResponse {
	private Meta meta;
	private String action;
	
	public Meta getMeta() {
		return meta;
	}
	
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
