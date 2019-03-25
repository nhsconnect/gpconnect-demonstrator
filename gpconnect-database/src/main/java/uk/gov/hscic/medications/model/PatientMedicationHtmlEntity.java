package uk.gov.hscic.medications.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import static uk.gov.hscic.patient.encounters.model.EncounterEntity.convertTimestamp2Date;

@Entity
@Table(name = "medications_html")
public class PatientMedicationHtmlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nhsNumber")
    private String nhsNumber;

    @Column(name = "currentRepeatPast")
    private String currentRepeatPast;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "medicationItem")
    private String medicationItem;

    @Column(name = "dosageInstruction")
    private String dosageInstruction;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "scheduledEnd")
    private String scheduledEnd;

    @Column(name = "daysDuration")
    private Integer daysDuration;

    @Column(name = "details")
    private String details;

    @Column(name = "lastIssued")
    private Date lastIssued;

    @Column(name = "reviewDate")
    private Date reviewDate;

    @Column(name = "numberIssued")
    private Integer numberIssued;

    @Column(name = "maxIssues")
    private Integer maxIssues;

    @Column(name = "typeMed")
    private String typeMed;

    @Column(name = "discontinuationReason")
    private String discontinuationReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getCurrentRepeatPast() {
        return currentRepeatPast;
    }

    public void setCurrentRepeatPast(String currentRepeatPast) {
        this.currentRepeatPast = currentRepeatPast;
    }

    public Date getStartDate() {
        return convertTimestamp2Date(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getMedicationItem() {
        return medicationItem;
    }

    public void setMedicationItem(String medicationItem) {
        this.medicationItem = medicationItem;
    }

    public String getScheduledEnd() {
        return scheduledEnd;
    }

    public void setScheduledEnd(String scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public Integer getDaysDuration() {
        return daysDuration;
    }

    public void setDaysDuration(int daysDuration) {
        this.daysDuration = daysDuration;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getLastIssued() {
        return convertTimestamp2Date(lastIssued);
    }

    public void setLastIssued(Date lastIssued) {
        this.lastIssued = lastIssued;
    }

    public Date getReviewDate() {
        return convertTimestamp2Date(reviewDate);
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getNumberIssued() {
        return numberIssued;
    }

    public void setNumberIssued(Integer numberIssued) {
        this.numberIssued = numberIssued;
    }

    public Integer getMaxIssues() {
        return maxIssues;
    }

    public void setMaxIssues(Integer maxIssues) {
        this.maxIssues = maxIssues;
    }

    public String getTypeMed() {
        return typeMed;
    }

    public void setTypeMed(String typeMed) {
        this.typeMed = typeMed;
    }

    public String getDosageInstruction() {
        return dosageInstruction;
    }

    // 0.7 additions
    public void setDosageInstruction(String dosageInstruction) {
        this.dosageInstruction = dosageInstruction;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscontinuationReason() {
        return discontinuationReason;
    }

    public void setDiscontinuationReason(String discontinuationReason) {
        this.discontinuationReason = discontinuationReason;
    }

}
