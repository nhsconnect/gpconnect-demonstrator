package uk.gov.hscic.patient.immunisations.model;

import java.util.Date;
import javax.persistence.*;
import static uk.gov.hscic.patient.encounters.model.EncounterEntity.convertTimestamp2Date;

@Entity
@Table(name = "immunisations")
public class ImmunisationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateOfVac")
    private Date dateOfVac;

    @Column(name = "nhsNumber")
    private String nhsNumber;

    @Column(name = "vaccination")
    private String vaccination;

    @Column(name = "part")
    private Integer part;

    @Column(name = "contents")
    private String contents;

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

    public Date getDateOfVac() {
        return convertTimestamp2Date(dateOfVac);
    }

    public void setDateOfVac(Date dateOfVac) {
        this.dateOfVac = dateOfVac;
    }

    public String getVaccination() {
        return vaccination;
    }

    public void setVaccination(String vaccination) {
        this.vaccination = vaccination;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
