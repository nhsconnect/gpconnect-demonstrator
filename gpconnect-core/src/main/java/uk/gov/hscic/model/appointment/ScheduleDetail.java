package uk.gov.hscic.model.appointment;

import java.util.Date;

public class ScheduleDetail {
    private Long id;
    private Long practitionerId;
    private String identifier;
    private String typeCode;
    private String typeDescription;
    private Long locationId;
    private String practitionerRoleCode;
    private String practitionerRoleDisplay;
    private Date startDateTime;
    private Date endDateTime;
    private String comment;
    private Date lastUpdated;
    private Long serviceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPractitionerId() {
        return practitionerId;
    }

    public void setPractitionerId(Long practitionerId) {
        this.practitionerId = practitionerId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    
	public String getPractitionerRoleCode() {
		return practitionerRoleCode;
	}

	public void setPractitionerRoleCode(String practitionerRoleCode) {
		this.practitionerRoleCode = practitionerRoleCode;
	}

    public String getPractitionerRoleDisplay() {
		return practitionerRoleDisplay;
	}

	public void setPractitionerRoleDisplay(String practitionerRoleDisplay) {
		this.practitionerRoleDisplay = practitionerRoleDisplay;
	}

	public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
