package uk.gov.hscic.medications;

import java.util.Date;

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
	
	@Column(name = "dateWritten")
	private Date dateWritten;
	
	@Column(name = "authorReferenceUrl")
	private String authorReferenceUrl;
	
	@Column(name = "authorId")
	private Long authorId;

	@Column(name = "noteText")
	private String noteText;
	

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Date getDateWritten() {
		return dateWritten;
	}

	public void setDateWritten(Date dateWritten) {
		this.dateWritten = dateWritten;
	}

	public String getAuthorReferenceUrl() {
		return authorReferenceUrl;
	}

	public void setAuthorReferenceUrl(String authorReferenceUrl) {
		this.authorReferenceUrl = authorReferenceUrl;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getNoteText() {
		return noteText;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}
}
