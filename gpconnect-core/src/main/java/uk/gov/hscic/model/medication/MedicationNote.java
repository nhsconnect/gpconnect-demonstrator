package uk.gov.hscic.model.medication;

import java.util.Date;

public class MedicationNote {

	private Long id;
	private Date dateWritten;
	private String authorReferenceUrl;
	private Long authorId;
	private String note;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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

	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
}
