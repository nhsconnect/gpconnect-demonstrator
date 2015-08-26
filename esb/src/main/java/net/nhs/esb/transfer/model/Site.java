package net.nhs.esb.transfer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "sites")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
    private Long id;

    @Column(name = "patient_id", unique = true, nullable = false)
    @JsonProperty("patientId")
    private Long patientId;

    @Column(name = "site_to")
    @JsonProperty("siteTo")
    private String siteTo;

    @Column(name = "site_from")
    @JsonProperty("siteFrom")
    private String siteFrom;

    @Column(name = "time_stamp")
    @JsonIgnore
    private String timeStamp;

    @OneToOne
    @JoinColumn(name = "transfer_detail_id")
    @JsonIgnore
    private TransferDetail transferDetail;

    @PrePersist
    public void setTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        timeStamp = sdf.format(GregorianCalendar.getInstance().getTime());
    }

    @Transient
    @JsonProperty("timeStamp")
    public Date getJsonTimestamp() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            return sdf.parse(timeStamp);
        } catch (ParseException ignore) {
            return null;
        }
    }


    public String getSiteTo() {
        return siteTo;
    }

    public void setSiteTo(String siteTo) {
        this.siteTo = siteTo;
    }

    public String getSiteFrom() {
        return siteFrom;
    }

    public void setSiteFrom(String siteFrom) {
        this.siteFrom = siteFrom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public TransferDetail getTransferDetail() {
        return transferDetail;
    }

    public void setTransferDetail(TransferDetail transferDetail) {
        this.transferDetail = transferDetail;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
