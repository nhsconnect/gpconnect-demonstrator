package uk.gov.hscic.model.location;

import java.io.Serializable;
import java.util.Date;

public class LocationDetails implements Serializable {
    private Long id;
    private String name;
    private String orgOdsCode;
    private String orgOdsCodeName;
    private String siteOdsCode;
    private String siteOdsCodeName;
    private String status;
    private Date lastUpdated;
    private String addressLine;
    private String addressCity;
    private String addressDistrict;
    private String addressState;
    private String addressPostalCode;
    private String addressCountry;

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

    public String getOrgOdsCode() {
        return orgOdsCode;
    }

    public void setOrgOdsCode(String orgOdsCode) {
        this.orgOdsCode = orgOdsCode;
    }

    public String getOrgOdsCodeName() {
        return orgOdsCodeName;
    }

    public void setOrgOdsCodeName(String orgOdsCodeName) {
        this.orgOdsCodeName = orgOdsCodeName;
    }

    public String getSiteOdsCode() {
        return siteOdsCode;
    }

    public void setSiteOdsCode(String siteOdsCode) {
        this.siteOdsCode = siteOdsCode;
    }

    public String getSiteOdsCodeName() {
        return siteOdsCodeName;
    }

    public void setSiteOdsCodeName(String siteOdsCodeName) {
        this.siteOdsCodeName = siteOdsCodeName;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getStatus(){
        return status;
    }
  
	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressDistrict() {
		return addressDistrict;
	}

	public void setAddressDistrict(String addressDistrict) {
		this.addressDistrict = addressDistrict;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}
}
