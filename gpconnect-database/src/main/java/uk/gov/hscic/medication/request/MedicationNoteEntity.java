package uk.gov.hscic.medication.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "medication_notes")
public class MedicationNoteEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "note")
	private String note;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
