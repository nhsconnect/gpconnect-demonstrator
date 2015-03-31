package net.nhs.esb.medication.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.nhs.esb.transfer.model.TransferOfCare;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
@Entity
@Table(name="medications", schema="poc_legacy")
public class Medication {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
    private Long id;
	
	@ManyToOne
    @JoinColumn(name="transfer_care_id")
    @JsonIgnore
    private TransferOfCare transferOfCare;
	
	@Column(name="name")
    @JsonProperty("name")
    private String name;
	
	@Column(name="code")
    @JsonProperty("code")
    private String code;
	
	@Column(name="terminology")
    @JsonProperty("terminology")
    private String terminology;
	
	@Column(name="route")
    @JsonProperty("route")
    private String route;
	
	@Column(name="dose_amount")
    @JsonProperty("doseAmount")
    private String doseAmount;
	
	@Column(name="dose_timing")
    @JsonProperty("doseTiming")
    private String doseTiming;
	
	@Column(name="start_date_time")
    @JsonProperty("startDateTime")
    private String startDateTime;
	
	@Column(name="dose_directions")
    @JsonProperty("doseDirections")
    private String doseDirections;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTerminology() {
        return terminology;
    }

    public void setTerminology(String terminology) {
        this.terminology = terminology;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDoseAmount() {
        return doseAmount;
    }

    public void setDoseAmount(String doseAmount) {
        this.doseAmount = doseAmount;
    }

    public String getDoseTiming() {
        return doseTiming;
    }

    public void setDoseTiming(String doseTiming) {
        this.doseTiming = doseTiming;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getDoseDirections() {
        return doseDirections;
    }

    public void setDoseDirections(String doseDirections) {
        this.doseDirections = doseDirections;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransferOfCare getTransferOfCare() {
		return transferOfCare;
	}

	public void setTransferOfCare(TransferOfCare transferOfCare) {
		this.transferOfCare = transferOfCare;
	}
}
