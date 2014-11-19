package net.nhs.esb.rest.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EhrProblemDiagnosis {
	@JsonProperty("date_of_onset")
	private List<String> dateOfOnset;
	private List<String> description;
	@JsonProperty("problem_diagnosis")
	private List<String> problemDiagnosis;
	
	public List<String> getDateOfOnset() {
		return dateOfOnset;
	}
	
	public void setDateOfOnset(List<String> dateOfOnset) {
		this.dateOfOnset = dateOfOnset;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public List<String> getProblemDiagnosis() {
		return problemDiagnosis;
	}

	public void setProblemDiagnosis(List<String> problemDiagnosis) {
		this.problemDiagnosis = problemDiagnosis;
	}
}
