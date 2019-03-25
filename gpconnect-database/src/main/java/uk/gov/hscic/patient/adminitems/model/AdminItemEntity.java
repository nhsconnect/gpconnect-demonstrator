package uk.gov.hscic.patient.adminitems.model;

import java.util.Date;
import javax.persistence.*;
import static uk.gov.hscic.patient.encounters.model.EncounterEntity.convertTimestamp2Date;

@Entity
@Table(name = "adminitems")
public class AdminItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nhsNumber")
    private String nhsNumber;

    @Column(name = "sectionDate")
    private Date sectionDate;

    @Column(name = "adminDate")
    private Date adminDate;

    @Column(name = "entry")
    private String entry;

    @Column(name = "details")
    private String details;

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public Date getSectionDate() {
        return convertTimestamp2Date(sectionDate);
    }

    public void setSectionDate(Date sectionDate) {
        this.sectionDate = sectionDate;
    }

    public Date getAdminDate() {
        return convertTimestamp2Date(adminDate);
    }

    public void setAdminDate(Date adminDate) {
        this.adminDate = adminDate;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
