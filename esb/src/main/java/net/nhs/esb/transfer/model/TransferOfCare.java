package net.nhs.esb.transfer.model;

import java.util.List;

import javax.persistence.*;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.problem.model.Problem;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="transfer_cares")
public class TransferOfCare {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty("id")
	private Long id;

	@OneToOne(mappedBy="transferOfCare", cascade=CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
    private TransferDetail transferDetail;
    
	@OneToMany(mappedBy="transferOfCare", cascade=CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<Allergy> allergies;
	
	@OneToMany(mappedBy="transferOfCare", cascade=CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<Contact> contacts;
	
	@OneToMany(mappedBy="transferOfCare", cascade=CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<Medication> medication;
	
	@OneToMany(mappedBy="transferOfCare", cascade=CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<Problem> problems;
    
    @ManyToOne
    @JoinColumn(name="transfer_care_composition_id")
    @JsonIgnore
    private TransferOfCareComposition transferOfCareComposition;

    @Transient
    private String source;

    public TransferDetail getTransferDetail() {
        return transferDetail;
    }

    public void setTransferDetail(TransferDetail transferDetail) {
        this.transferDetail = transferDetail;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Medication> getMedication() {
        return medication;
    }

    public void setMedication(List<Medication> medication) {
        this.medication = medication;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransferOfCareComposition getTransferOfCareComposition() {
		return transferOfCareComposition;
	}

	public void setTransferOfCareComposition(
			TransferOfCareComposition transferOfCareComposition) {
		this.transferOfCareComposition = transferOfCareComposition;
	}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
