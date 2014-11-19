package net.nhs.legacy.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author robert
 */
@Entity
@Table(name = "event")
@XmlRootElement
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "event_id")
    private Integer eventId;
    @Basic(optional = false)
    @Column(name = "event_guid")
    private String eventGuid;
    @Basic(optional = false)
    @Column(name = "patient_id")
    private int patientId;
    @Basic(optional = false)
    @Column(name = "patient_guid")
    private String patientGuid;
    @Column(name = "diagnosis_progression_date")
    @Temporal(TemporalType.DATE)
    private Date diagnosisProgressionDate;
    @Column(name = "diagnosis")
    private String diagnosis;
    @Column(name = "primary_site")
    private String primarySite;
    @Column(name = "grade")
    private String grade;

    public Event() {
    }

    public Event(Integer eventId) {
        this.eventId = eventId;
    }

    public Event(Integer eventId, String eventGuid, int patientId, String patientGuid) {
        this.eventId = eventId;
        this.eventGuid = eventGuid;
        this.patientId = patientId;
        this.patientGuid = patientGuid;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventGuid() {
        return eventGuid;
    }

    public void setEventGuid(String eventGuid) {
        this.eventGuid = eventGuid;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientGuid() {
        return patientGuid;
    }

    public void setPatientGuid(String patientGuid) {
        this.patientGuid = patientGuid;
    }

    public Date getDiagnosisProgressionDate() {
        return diagnosisProgressionDate;
    }

    public void setDiagnosisProgressionDate(Date diagnosisProgressionDate) {
        this.diagnosisProgressionDate = diagnosisProgressionDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrimarySite() {
        return primarySite;
    }

    public void setPrimarySite(String primarySite) {
        this.primarySite = primarySite;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Event[ eventId=" + eventId + " ]";
    }
    
}