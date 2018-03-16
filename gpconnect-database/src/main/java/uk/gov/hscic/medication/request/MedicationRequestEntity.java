package uk.gov.hscic.medication.request;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uk.gov.hscic.medications.MedicationNoteEntity;
import uk.gov.hscic.medications.MedicationReasonCodeEntity;
import uk.gov.hscic.medications.MedicationReasonReferenceEntity;

@Entity
@Table(name = "medication_requests")
public class MedicationRequestEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "medication_request_based_on_references", joinColumns = {
            @JoinColumn(name = "medicationRequestId", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "basedOnReferenceId", referencedColumnName = "id") })
	private List<MedicationRequestBasedOnReferenceEntity> basedOnReferences;
	
	@Column(name = "groupIdentifier")
	private String groupIdentifier;
	
	@Column(name = "statusCode")
	private String statusCode;

	@Column(name = "statusDisplay")
	private String statusDisplay;
	 
	@Column(name = "intentCode")
	private String intentCode;
	
	@Column(name = "intentDisplay")
	private String intentDisplay;
	
	@Column(name = "medicationId")
	private Long medicationId;
	
	@Column(name = "patientId")
	private Long patientId;
	
	@Column(name = "encounterId")
	private Long encounterId;
	
	@Column(name = "authoredOn")
	private Date authoredOn;
	
	@Column(name = "requesterUrl")
	private String requesterUrl;
	
	@Column(name = "requesterId")
	private Long requesterId;
	
	@Column(name = "authorisingPractitionerId")
	private Long authorisingPractitionerId;
	
	@ManyToMany
    @JoinTable(name = "medication_request_reason_codes", joinColumns = {
            @JoinColumn(name = "medicationRequestId", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "reasonCodeId", referencedColumnName = "id") })
    private List<MedicationReasonCodeEntity> reasonCodes;
	
	@ManyToMany
	   @JoinTable(name = "medication_request_reason_references", joinColumns = {
	           @JoinColumn(name = "medicationRequestId", referencedColumnName = "id") }, inverseJoinColumns = {
	                   @JoinColumn(name = "reasonReferenceId", referencedColumnName = "id") })
    private List<MedicationReasonReferenceEntity> reasonReferences;
	
	@ManyToMany
    @JoinTable(name = "medication_request_notes", joinColumns = {
            @JoinColumn(name = "medicationRequestId", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "noteId", referencedColumnName = "id") })
    private List<MedicationNoteEntity> notes;
	
	@Column(name = "dosageText")
    private String dosageText;
    
    @Column(name = "dosageInstruction")
    private String dosageInstruction;
    
    @Column(name = "dispenseRequestStartDate")
    private Date dispenseRequestStartDate;
    
    @Column(name = "dispenseRequestEndDate")
    private Date dispenseRequestEndDate;
    
    @Column(name = "dispenseQuantityValue")
    private String dispenseQuantityValue;
    
    @Column(name = "dispenseQuantityUnit")
    private String dispenseQuantityUnit;
    
    //dispense quantity code and system?
    
    @Column(name = "dispenseQuantityText")
    private String dispenseQuantityText;
    
    @Column(name = "expectedSupplyDuration")
    private String expectedSupplyDuration;
    
    @Column(name = "dispenseRequestOrganizationId")
    private Long dispenseRequestOrganizationId;
    
    @Column(name = "priorMedicationRequestId")
    private Long priorMedicationRequestId;
    
    @Column(name = "numberOfRepeatPrescriptionsAllowed")
    private Integer numberOfRepeatPrescriptionsAllowed;
    
    @Column(name = "numberOfRepeatPrescriptionsIssued")
    private Integer numberOfRepeatPrescriptionsIssued;
    
    @Column(name = "authorisationExpiryDate")
    private Date authorisationExpiryDate;
    
    @Column(name = "prescriptionTypeCode")
    private String prescriptionTypeCode;
    
    @Column(name = "prescriptionTypeDisplay")
    private String prescriptionTypeDisplay;    
    
    @Column(name = "statusReasonDate")
    private Date statusReasonDate;	
    
    @Column(name = "statusReasonCode")
    private String statusReasonCode;
    
    @Column(name = "statusReasonValue")
    private String statusReasonValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<MedicationRequestBasedOnReferenceEntity> getBasedOnReferences() {
		return basedOnReferences;
	}

	public void setBasedOnReferences(List<MedicationRequestBasedOnReferenceEntity> basedOnReferences) {
		this.basedOnReferences = basedOnReferences;
	}

	public String getGroupIdentifier() {
		return groupIdentifier;
	}

	public void setGroupIdentifier(String groupIdentifier) {
		this.groupIdentifier = groupIdentifier;
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

	public String getIntentCode() {
		return intentCode;
	}

	public void setIntentCode(String intentCode) {
		this.intentCode = intentCode;
	}

	public String getIntentDisplay() {
		return intentDisplay;
	}

	public void setIntentDisplay(String intentDisplay) {
		this.intentDisplay = intentDisplay;
	}

	public Long getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(Long medicationId) {
		this.medicationId = medicationId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Long encounterId) {
		this.encounterId = encounterId;
	}

	public Date getAuthoredOn() {
		return authoredOn;
	}

	public void setAuthoredOn(Date authoredOn) {
		this.authoredOn = authoredOn;
	}

	public String getRequesterUrl() {
		return requesterUrl;
	}

	public void setRequesterUrl(String requesterUrl) {
		this.requesterUrl = requesterUrl;
	}

	public Long getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	public Long getAuthorisingPractitionerId() {
		return authorisingPractitionerId;
	}

	public void setAuthorisingPractitionerId(Long authorisingPractitionerId) {
		this.authorisingPractitionerId = authorisingPractitionerId;
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

	public Date getDispenseRequestStartDate() {
		return dispenseRequestStartDate;
	}

	public void setDispenseRequestStartDate(Date dispenseRequestStartDate) {
		this.dispenseRequestStartDate = dispenseRequestStartDate;
	}

	public Date getDispenseRequestEndDate() {
		return dispenseRequestEndDate;
	}

	public void setDispenseRequestEndDate(Date dispenseRequestEndDate) {
		this.dispenseRequestEndDate = dispenseRequestEndDate;
	}

	public String getDispenseQuantityValue() {
		return dispenseQuantityValue;
	}

	public void setDispenseQuantityValue(String dispenseQuantityValue) {
		this.dispenseQuantityValue = dispenseQuantityValue;
	}

	public String getDispenseQuantityUnit() {
		return dispenseQuantityUnit;
	}

	public void setDispenseQuantityUnit(String dispenseQuantityUnit) {
		this.dispenseQuantityUnit = dispenseQuantityUnit;
	}

	public String getDispenseQuantityText() {
		return dispenseQuantityText;
	}

	public void setDispenseQuantityText(String dispenseQuantityText) {
		this.dispenseQuantityText = dispenseQuantityText;
	}

	public String getExpectedSupplyDurationValue() {
		return expectedSupplyDuration;
	}

	public void setExpectedSupplyDurationValue(String expectedSupplyDuration) {
		this.expectedSupplyDuration = expectedSupplyDuration;
	}

	public Long getDispenseRequestOrganizationId() {
		return dispenseRequestOrganizationId;
	}

	public void setDispenseRequestOrganizationId(Long dispenseRequestOrganizationId) {
		this.dispenseRequestOrganizationId = dispenseRequestOrganizationId;
	}

	public Long getPriorMedicationRequestId() {
		return priorMedicationRequestId;
	}

	public void setPriorMedicationRequestId(Long priorMedicationRequestId) {
		this.priorMedicationRequestId = priorMedicationRequestId;
	}

	public Integer getNumberOfRepeatPrescriptionsAllowed() {
		return numberOfRepeatPrescriptionsAllowed;
	}

	public void setNumberOfRepeatPrescriptionsAllowed(Integer numberOfRepeatPrescriptionsAllowed) {
		this.numberOfRepeatPrescriptionsAllowed = numberOfRepeatPrescriptionsAllowed;
	}

	public Integer getNumberOfRepeatPrescriptionsIssued() {
		return numberOfRepeatPrescriptionsIssued;
	}

	public void setNumberOfRepeatPrescriptionsIssued(Integer numberOfRepeatPrescriptionsIssued) {
		this.numberOfRepeatPrescriptionsIssued = numberOfRepeatPrescriptionsIssued;
	}

	public Date getAuthorisationExpiryDate() {
		return authorisationExpiryDate;
	}

	public void setAuthorisationExpiryDate(Date authorisationExpiryDate) {
		this.authorisationExpiryDate = authorisationExpiryDate;
	}

	public String getPrescriptionTypeCode() {
		return prescriptionTypeCode;
	}

	public void setPrescriptionTypeCode(String prescriptionTypeCode) {
		this.prescriptionTypeCode = prescriptionTypeCode;
	}

	public String getPrescriptionTypeDisplay() {
		return prescriptionTypeDisplay;
	}

	public void setPrescriptionTypeDisplay(String prescriptionTypeDisplay) {
		this.prescriptionTypeDisplay = prescriptionTypeDisplay;
	}

	public Date getStatusReasonDate() {
		return statusReasonDate;
	}

	public void setStatusReasonDate(Date statusReasonDate) {
		this.statusReasonDate = statusReasonDate;
	}

	public String getStatusReasonCode() {
		return statusReasonCode;
	}

	public void setStatusReasonCode(String statusReasonCode) {
		this.statusReasonCode = statusReasonCode;
	}

	public String getStatusReasonValue() {
		return statusReasonValue;
	}

	public void setStatusReasonValue(String statusReasonValue) {
		this.statusReasonValue = statusReasonValue;
	}
    
}
