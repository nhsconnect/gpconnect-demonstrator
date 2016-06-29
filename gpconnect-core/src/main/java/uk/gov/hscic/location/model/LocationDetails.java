package uk.gov.hscic.location.model;

import java.io.Serializable;
import java.net.URI;

public class LocationDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private URI odsSiteCode;
	private String odsSiteCodeName;
	private LocationDetails managingOrganisation;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public URI getOdsSiteCode() {
		return odsSiteCode;
	}

	public void setOdsSiteCode(URI odsSiteCode) {
		this.odsSiteCode = odsSiteCode;
	}

	public String getOdsSiteCodeName() {
		return odsSiteCodeName;
	}

	public void setOdsSiteCodeName(String odsSiteCodeName) {
		this.odsSiteCodeName = odsSiteCodeName;
	}

	public LocationDetails getManagingOrganisation() {
		return managingOrganisation;
	}

	public void setManagingOrganisation(LocationDetails managingOrganisation) {
		this.managingOrganisation = managingOrganisation;
	}
	
	
}
