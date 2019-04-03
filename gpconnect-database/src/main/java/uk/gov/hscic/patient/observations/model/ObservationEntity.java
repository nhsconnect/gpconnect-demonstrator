package uk.gov.hscic.patient.observations.model;

import java.util.Date;
import javax.persistence.*;
import static uk.gov.hscic.patient.encounters.model.EncounterEntity.convertTimestamp2Date;

@Entity
@Table(name = "observations")
public class ObservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nhsNumber")
    private String nhsNumber;

    @Column(name = "observationDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date observationDate;

    @Column(name = "entry")
    private String entry;

    @Column(name = "value")
    private String value;

    // added at 0.7.1 NB reserved word..
    @Column(name = "theRange")
    private String range;

    @Column(name = "details")
    private String details;

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getObservationDate() {
        return convertTimestamp2Date(observationDate);
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(String range) {
        this.range = range;
    }
}
