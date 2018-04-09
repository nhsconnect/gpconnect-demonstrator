package uk.gov.hscic.model.appointment;

import java.util.Date;
import java.util.List;


public class AppointmentDetail {
    private String cancellationReason;
    private String comment;
    private String description;
    private Date endDateTime;
    private Long id;
    private Date lastUpdated;
    private Long locationId;
    private Integer minutesDuration;
    private Long patientId;
    private Long practitionerId;
    private Integer priority;
    private List<Long> slotIds;
    private Date startDateTime;
    private String status;
    private Long typeCode;
    private String typeDisplay;
    private BookingOrgDetail bookingOrganization;
    private Date created;

    private String typeText;

    public String getCancellationReason() {
        return cancellationReason;
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {
        return description;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public Long getId() {
        return id;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Long getLocationId() {
        return locationId;
    }

    public Integer getMinutesDuration() {
        return minutesDuration;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getPractitionerId() {
        return practitionerId;
    }

    public Integer getPriority() {
        return priority;
    }

    public List<Long> getSlotIds() {
        return slotIds;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public String getStatus() {
        return status;
    }

    public Long getTypeCode() {
        return typeCode;
    }

    public String getTypeDisplay() {
        return typeDisplay;
    }

    public String getTypeText() {
        return typeText;
    }

    public BookingOrgDetail getBookingOrganization() {
        return bookingOrganization;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setMinutesDuration(Integer integer) {
        this.minutesDuration = integer;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setPractitionerId(Long practitionerId) {
        this.practitionerId = practitionerId;
    }

    public void setPriority(Integer integer) {
        this.priority = integer;
    }

    public void setSlotIds(List<Long> slotIds) {
        this.slotIds = slotIds;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTypeCode(Long typeCode) {
        this.typeCode = typeCode;
    }

    public void setTypeDisplay(String typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public void setBookingOrganization(BookingOrgDetail bookingOrganization) {
        this.bookingOrganization = bookingOrganization;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


}
