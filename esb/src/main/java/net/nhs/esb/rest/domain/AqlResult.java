package net.nhs.esb.rest.domain;

public class AqlResult {
	private String uid;
	private String problemDiagnosis;
	private String description;
	private String dateOfOnset;
	private String severity;
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
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

	public String getDateOfOnset() {
		return dateOfOnset;
	}

	public void setDateOfOnset(String dateOfOnset) {
		this.dateOfOnset = dateOfOnset;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
}
