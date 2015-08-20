package net.nhs.domain.openehr.model;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author robert
 */
@XmlRootElement
public class Diagnoses implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer patientId;
    private String problemDiagnosis;
    private String description;
    private String severity;
    private Date dateOfOnset;
    private Integer ageAtOnset;
    private String bodySite;
    private Date dateOfResolution;
    private Integer ageAtResolution;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getProblemDiagnosis() {
        return problemDiagnosis;
    }

    public void setProblemDiagnosis(String problemDiagnosis) {
        this.problemDiagnosis = problemDiagnosis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Date getDateOfOnset() {
        return dateOfOnset;
    }

    public void setDateOfOnset(Date dateOfOnset) {
        this.dateOfOnset = dateOfOnset;
    }

    public Integer getAgeAtOnset() {
        return ageAtOnset;
    }

    public void setAgeAtOnset(Integer ageAtOnset) {
        this.ageAtOnset = ageAtOnset;
    }

    public String getBodySite() {
        return bodySite;
    }

    public void setBodySite(String bodySite) {
        this.bodySite = bodySite;
    }

    public Date getDateOfResolution() {
        return dateOfResolution;
    }

    public void setDateOfResolution(Date dateOfResolution) {
        this.dateOfResolution = dateOfResolution;
    }

    public Integer getAgeAtResolution() {
        return ageAtResolution;
    }

    public void setAgeAtResolution(Integer ageAtResolution) {
        this.ageAtResolution = ageAtResolution;
    }
}
