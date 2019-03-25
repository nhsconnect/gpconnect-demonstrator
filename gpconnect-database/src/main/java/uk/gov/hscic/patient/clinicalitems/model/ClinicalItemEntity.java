package uk.gov.hscic.patient.clinicalitems.model;

import java.util.Date;
import javax.persistence.*;
import static uk.gov.hscic.patient.encounters.model.EncounterEntity.convertTimestamp2Date;

@Entity
@Table(name = "clinicalitems")
public class ClinicalItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nhsNumber")
    private String nhsNumber;

    @Column(name = "sectionDate")
    private Date sectionDate;

    @Column(name = "dateOfItem")
    private Date dateOfItem;

    @Column(name = "entry")
    private String entry;

    @Column(name = "details")
    private String details;

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

    // scf Commented out the sectionDate is not referenced anywhere in the code and appears to be a duplicate of date
//    public Date getSectionDate() {
//        return convertTimestamp2Date(sectionDate);
//    }
//
//    public void setSectionDate(Date sectionDate) {
//        this.sectionDate = sectionDate;
//    }

    public Date getDate() {
        return convertTimestamp2Date(dateOfItem);
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDate(Date date) {
        this.dateOfItem = date;
    }

}
