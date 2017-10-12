package uk.gov.hscic.appointment.appointment;

import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import uk.gov.hscic.appointment.bookingOrganization.BookingOrgEntity;
import uk.gov.hscic.appointment.slot.SlotEntity;

@Entity
@Table(name = "appointment_appointments")
@Cacheable(false)
public class AppointmentEntity {

    @Column(name = "cancellationReason")
    private String cancellationReason;

    @Column(name = "commentText")
    private String comment;

    @Column(name = "description")
    private String description;

    @Column(name = "endDateTime")
    private Date endDateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lastUpdated")
    private Date lastUpdated;

    @Column(name = "locationId")
    private Long locationId;

    @Column(name = "minutesDuration")
    private Integer minutesDuration;

    @Column(name = "patientId")
    private Long patientId;

    @Column(name = "practitionerId")
    private Long practitionerId;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "reasonCode")
    private String reasonCode;

    @Column(name = "reasonDisplay")
    private String reasonDisplay;

    @Column(name = "reasonURL")
    private String reasonURL;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "appointment_appointments_slots", joinColumns = {
            @JoinColumn(name = "appointmentId", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "slotId", referencedColumnName = "id") })
    private List<SlotEntity> slots;

    @Column(name = "startDateTime")
    private Date startDateTime;

    @Column(name = "status")
    private String status;

    @Column(name = "typeCode")
    private Long typeCode;

    @Column(name = "typeDisplay")
    private String typeDisplay;

    @Column(name = "typeText")
    private String typeText;
      
    //@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "id")
    //@JoinColumn(name = "id")
    @OneToOne(cascade=CascadeType.ALL, mappedBy="appointmentEntity") 
    private BookingOrgEntity bookingOrganization;
    
    @Column(name = "created")
    private Date created;
        

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

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonDisplay() {
        return reasonDisplay;
    }

    public String getReasonURL() {
        return reasonURL;
    }

    public List<SlotEntity> getSlots() {
        return slots;
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
    
    public BookingOrgEntity getBookingOrganization(){
        return bookingOrganization;
    }
    
    public Date getCreated() {
        return created;
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

    public void setMinutesDuration(Integer minutesDuration) {
        this.minutesDuration = minutesDuration;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setPractitionerId(Long practitionerId) {
        this.practitionerId = practitionerId;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public void setReasonDisplay(String reasonDisplay) {
        this.reasonDisplay = reasonDisplay;
    }

    public void setReasonURL(String reasonURL) {
        this.reasonURL = reasonURL;
    }

    public void setSlots(List<SlotEntity> slots) {
        this.slots = slots;
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
    
    public void setBookingOrganization(BookingOrgEntity bookingOrganization){
        this.bookingOrganization = bookingOrganization;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
}
