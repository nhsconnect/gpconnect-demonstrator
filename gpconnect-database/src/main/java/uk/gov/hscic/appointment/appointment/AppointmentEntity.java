package uk.gov.hscic.appointment.appointment;

import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @Column(name = "extensionBookCode")
    private String extensionBookCode;

    @Column(name = "extensionBookDisplay")
    private String extensionBookDisplay;

    @Column(name = "extensionBookURL")
    private String extensionBookURL;

    @Column(name = "extensionCatCode")
    private String extensionCatCode;

    @Column(name = "extensionCatDisplay")
    private String extensionCatDisplay;

    @Column(name = "extensionCatURL")
    private String extensionCatURL;

    @Column(name = "extensionConCode")
    private String extensionConCode;

    @Column(name = "extensionConDisplay")
    private String extensionConDisplay;

    @Column(name = "extensionConURL")
    private String extensionConURL;

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

    public String getExtensionBookCode() {
        return extensionBookCode;
    }

    public String getExtensionBookDisplay() {
        return extensionBookDisplay;
    }

    public String getExtensionBookURL() {
        return extensionBookURL;
    }

    public String getExtensionCatCode() {
        return extensionCatCode;
    }

    public String getExtensionCatDisplay() {
        return extensionCatDisplay;
    }

    public String getExtensionCatURL() {
        return extensionCatURL;
    }

    public String getExtensionConCode() {
        return extensionConCode;
    }

    public String getExtensionConDisplay() {
        return extensionConDisplay;
    }

    public String getExtensionConURL() {
        return extensionConURL;
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

    public void setExtensionBookCode(String extensionBookCode) {
        this.extensionBookCode = extensionBookCode;
    }

    public void setExtensionBookDisplay(String extensionBookDisplay) {
        this.extensionBookDisplay = extensionBookDisplay;
    }

    public void setExtensionBookURL(String extensionBookURL) {
        this.extensionBookURL = extensionBookURL;
    }

    public void setExtensionCatCode(String extensionCatCode) {
        this.extensionCatCode = extensionCatCode;
    }

    public void setExtensionCatDisplay(String extensionCatDisplay) {
        this.extensionCatDisplay = extensionCatDisplay;
    }

    public void setExtensionCatURL(String extensionCatURL) {
        this.extensionCatURL = extensionCatURL;
    }

    public void setExtensionConCode(String extensionConCode) {
        this.extensionConCode = extensionConCode;
    }

    public void setExtensionConDisplay(String extensionConDisplay) {
        this.extensionConDisplay = extensionConDisplay;
    }

    public void setExtensionConURL(String extensionConURL) {
        this.extensionConURL = extensionConURL;
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
}
