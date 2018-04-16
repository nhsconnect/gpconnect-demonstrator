package uk.gov.hscic.appointment.appointment;

import uk.gov.hscic.appointment.bookingOrganization.BookingOrgEntity;
import uk.gov.hscic.appointment.slot.SlotEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
