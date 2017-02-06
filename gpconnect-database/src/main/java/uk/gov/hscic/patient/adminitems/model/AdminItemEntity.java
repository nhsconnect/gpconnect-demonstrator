package uk.gov.hscic.patient.adminitems.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "adminitems")
public class AdminItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nhsNumber")
    private Long nhsNumber;

    @Column(name = "sectionDate")
    private Date sectionDate;

    @Column(name = "htmlPart")
    private String htmlPart;

    @Column(name = "provider")
    private String provider;

    @Column(name = "lastUpdated")
    private Date lastUpdated;

    public Long getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(Long nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public Date getSectionDate() {
        return sectionDate;
    }

    public void setSectionDate(Date sectionDate) {
        this.sectionDate = sectionDate;
    }

    public String getHtmlPart() {
        return htmlPart;
    }

    public void setHtmlPart(String htmlPart) {
        this.htmlPart = htmlPart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
