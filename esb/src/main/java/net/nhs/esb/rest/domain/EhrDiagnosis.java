package net.nhs.esb.rest.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EhrDiagnosis {
	@JsonProperty("problem_diagnosis")
	private List<EhrProblemDiagnosis> problemDiagnosis;

	public List<EhrProblemDiagnosis> getProblemDiagnosis() {
		return problemDiagnosis;
	}

	public void setProblemDiagnosis(List<EhrProblemDiagnosis> problemDiagnosis) {
		this.problemDiagnosis = problemDiagnosis;
	}
}
