package uk.gov.hscic.patient.encounters.model;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "encounters")
public class EncounterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nhsNumber")
    private String nhsNumber;

    @Column(name = "sectionDate")
    private Date sectionDate;

    @Column(name = "encounterDate")
    private Date encounterDate;

    @Column(name = "title")
    private String title;

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

    public Date getSectionDate() {
        return convertTimestamp2Date(sectionDate);
    }

    public void setSectionDate(Date sectionDate) {
        this.sectionDate = sectionDate;
    }

    public Date getEncounterDate() {
        return convertTimestamp2Date(encounterDate);
    }

    public void setEncounterDate(Date encounterDate) {
        this.encounterDate = encounterDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * convert to java.util.Date if its a Timestamp subclass
     *
     * @param dt
     * @return
     */
    public static java.util.Date convertTimestamp2Date(Date dt) {
        if (dt != null && dt instanceof java.sql.Timestamp) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dt.getTime());
            return calendar.getTime();
        } else {
            return dt;
        }
    }

}
