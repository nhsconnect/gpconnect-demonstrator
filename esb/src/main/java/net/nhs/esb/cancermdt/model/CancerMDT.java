package net.nhs.esb.cancermdt.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

/**
 */
@Entity
@Table(name = "cancermdts")
public class CancerMDT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
    private Long id;

    @Column(name = "compositionId")
    @JsonProperty("compositionId")
    private String compositionId;

    @ElementCollection
    @JsonProperty("rawComposition")
    Map<String, String> rawComposition;

     
    @Column(name = "service")
    @JsonProperty("Service")
    private String service;

    @Column(name = "date")
    @JsonProperty("Date")
    private String date;

    @Column(name = "notes")
    @JsonProperty("Notes")
    private String notes;

    @Column(name = "participation")
    @JsonProperty("participation")
    @OneToMany
    private List<CancerMDTparticipation> participation;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getRawComposition() {
        return rawComposition;
    }

    public void setRawComposition(Map<String, String> rawComposition) {
        this.rawComposition = rawComposition;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<CancerMDTparticipation> getParticipation() {
        return participation;
    }

    public void setParticipation(List<CancerMDTparticipation> participation) {
        this.participation = participation;
    }

}
