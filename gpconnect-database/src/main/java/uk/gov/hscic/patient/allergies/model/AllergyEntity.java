package uk.gov.hscic.patient.allergies.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import static uk.gov.hscic.patient.encounters.model.EncounterEntity.convertTimestamp2Date;

@Entity
@Table(name = "allergies")
public class AllergyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nhsNumber")
    private String nhsNumber;

    @Column(name = "currentOrHistoric")
    private String currentOrHistoric;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "details")
    private String details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentOrHistoric() {
        return currentOrHistoric;
    }

    public void setCurrentOrHistoric(String currentOrHistoric) {
        this.currentOrHistoric = currentOrHistoric;
    }

    public Date getStartDate() {
        return convertTimestamp2Date(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return convertTimestamp2Date(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }
}
