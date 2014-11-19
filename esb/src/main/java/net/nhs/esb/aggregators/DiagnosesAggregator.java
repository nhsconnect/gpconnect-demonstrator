package net.nhs.esb.aggregators;

import net.nhs.domain.openehr.model.Diagnoses;

import org.springframework.stereotype.Component;

@Component
public class DiagnosesAggregator {
	
	public Diagnoses aggregate(Diagnoses update, Diagnoses existing) {
		if(update.getProblemDiagnosis() != null) {
			existing.setProblemDiagnosis(update.getProblemDiagnosis());
		}
		
		if(update.getDescription() != null) {
			existing.setDescription(update.getDescription());
		}
		
		if(update.getSeverity() != null) {
			existing.setSeverity(update.getSeverity());
		}
		
		if(update.getDateOfOnset() != null) {
			existing.setDateOfOnset(update.getDateOfOnset());
		}
		
		if(update.getAgeAtOnset() != null) {
			existing.setAgeAtOnset(update.getAgeAtOnset());
		}
		
		if(update.getBodySite() != null) {
			existing.setBodySite(update.getBodySite());
		}
		
		if(update.getDateOfResolution() != null) {
			existing.setDateOfResolution(update.getDateOfResolution());
		}
		
		if(update.getAgeAtResolution() != null) {
			existing.setAgeAtResolution(update.getAgeAtResolution()); 
		}
		
		return existing;
	}
}
