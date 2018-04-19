package uk.gov.hscic.medications;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import uk.gov.hscic.patient.structuredAllergyIntolerance.StructuredAllergyIntoleranceEntity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "medications")
public class MedicationEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code")
    private String code;
    
    @Column(name = "display")
    private String display;
    
    @Column(name = "text")
    private String text;
    
    @Column(name = "batchNumber")
    private String batchNumber;
    
    @Column(name = "expiryDate")
    private Date expiryDate;
    
    @Column(name = "lastUpdated")
    private Date lastUpdated;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "medication_allergies", joinColumns = {
			@JoinColumn(name = "medicationId", referencedColumnName = "id") }, inverseJoinColumns = {
			@JoinColumn(name = "allergyintoleranceId", referencedColumnName = "id") })
	public List<StructuredAllergyIntoleranceEntity> medicationAllergies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<StructuredAllergyIntoleranceEntity> getMedicationAllergies() {
		return medicationAllergies;
	}

	public void setMedicationAllergies(List<StructuredAllergyIntoleranceEntity> medicationAllergies) {
		this.medicationAllergies = medicationAllergies;
	}
}
