package uk.gov.hscic.appointment.slot.model;

import java.util.Date;

public class SlotDetail {
    
    private Long Id;
    private Long typeCode;
    private String typeDisply;
    private Long scheduleReference;
    private String freeBusyType;
    private Date startDateTime;
    private Date endDateTime;

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Long getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Long typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDisply() {
        return typeDisply;
    }

    public void setTypeDisply(String typeDisply) {
        this.typeDisply = typeDisply;
    }

    public Long getScheduleReference() {
        return scheduleReference;
    }

    public void setScheduleReference(Long scheduleReference) {
        this.scheduleReference = scheduleReference;
    }

    public String getFreeBusyType() {
        return freeBusyType;
    }

    public void setFreeBusyType(String freeBusyType) {
        this.freeBusyType = freeBusyType;
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
    
}
