package uk.gov.hscic.medication.statement;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import uk.gov.hscic.medications.MedicationNoteEntity;
import uk.gov.hscic.medications.MedicationReasonCodeEntity;
import uk.gov.hscic.medications.MedicationReasonReferenceEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "medication_statements")
public class MedicationStatementEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "lastIssueDate")
	private Date lastIssueDate;
	
	@Column(name = "medicationRequestId")
    private String medicationRequestId;
	
    @Column(name = "statusCode")
    private String statusCode;
    
    @Column(name = "statusDisplay")
    private String statusDisplay;
    
    @Column(name = "medicationId")
    private Long medicationId;
    
    @Column(name = "startDate")
    private Date startDate;
    
    @Column(name = "endDate")
    private Date endDate;
    
    @Column(name = "dateAsserted")
    private Date dateAsserted;
    
    @Column(name = "patientId")
    private Long patientId;
    
    @Column(name = "takenCode")
    private String takenCode;
    
    @Column(name = "takenDisplay")
    private String takenDisplay;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "medication_statement_reason_codes", joinColumns = {
            @JoinColumn(name = "medicationStatementId", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "reasonCodeId", referencedColumnName = "id") })
    private List<MedicationReasonCodeEntity> reasonCodes;
    
    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "medication_statement_reason_references", joinColumns = {
            @JoinColumn(name = "medicationStatementId", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "reasonReferenceId", referencedColumnName = "id") })
    private List<MedicationReasonReferenceEntity> reasonReferences;
    
    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "medication_statement_notes", joinColumns = {
            @JoinColumn(name = "medicationStatementId", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "noteId", referencedColumnName = "id") })
    private List<MedicationNoteEntity> notes;
    
    @Column(name = "dosageText")
    private String dosageText;
    
    @Column(name = "dosageInstruction")
    private String dosageInstruction;
    
    @Column(name = "lastUpdated")
    private Date lastUpdated;

    @Column(name = "prescribingAgency")
    private String prescribingAgency;

    @Column(name = "guid")
    private String guid;

    @Column(name = "warningCode")
    private String warningCode;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPrescribingAgency() {
        return prescribingAgency;
    }

    public void setPrescribingAgency(String prescribingAgency) {
        this.prescribingAgency = prescribingAgency;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastIssueDate() {
		return lastIssueDate;
	}

	public void setLastIssueDate(Date lastIssueDate) {
		this.lastIssueDate = lastIssueDate;
	}

    public String getMedicationRequestId() {
		return medicationRequestId;
	}

    public void setMedicationRequestId(String medicationRequestId) {
		this.medicationRequestId = medicationRequestId;
	}
    
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDisplay() {
		return statusDisplay;
	}

	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}

	public Long getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(Long medicationId) {
		this.medicationId = medicationId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getDateAsserted() {
		return dateAsserted;
	}

	public void setDateAsserted(Date dateAsserted) {
		this.dateAsserted = dateAsserted;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getTakenCode() {
		return takenCode;
	}

	public void setTakenCode(String takenCode) {
		this.takenCode = takenCode;
	}

	public String getTakenDisplay() {
		return takenDisplay;
	}

	public void setTakenDisplay(String takenDisplay) {
		this.takenDisplay = takenDisplay;
	}

	public List<MedicationReasonCodeEntity> getReasonCodes() {
		return reasonCodes;
	}

	public void setReasonCodes(List<MedicationReasonCodeEntity> reasonCodes) {
		this.reasonCodes = reasonCodes;
	}

	public List<MedicationReasonReferenceEntity> getReasonReferences() {
		return reasonReferences;
	}

	public void setReasonReferences(List<MedicationReasonReferenceEntity> reasonReferences) {
		this.reasonReferences = reasonReferences;
	}

	public List<MedicationNoteEntity> getNotes() {
		return notes;
	}

	public void setNotes(List<MedicationNoteEntity> notes) {
		this.notes = notes;
	}

	public String getDosageText() {
		return dosageText;
	}

	public void setDosageText(String dosageText) {
		this.dosageText = dosageText;
	}

	public String getDosageInstruction() {
		return dosageInstruction;
	}

	public void setDosageInstruction(String dosageInstruction) {
		this.dosageInstruction = dosageInstruction;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getWarningCode() {
		return warningCode;
	}

	public void setWarningCode(String warningCode) {
		this.warningCode = warningCode;
	}
}
