package net.nhs.esb.enrichers;

import net.nhs.domain.openehr.model.Diagnoses;
import net.nhs.esb.rest.Patients;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component
public class DiagnosesEnricher {
	public Diagnoses enrich(@Body Diagnoses diagnoses, @Header(Patients.DIAGNOSIS_ID_HEADER) String diagnosisId, @Header(Patients.PATIENT_ID_HEADER) Integer patientId) {
		if(diagnosisId != null) {
			diagnoses.setId(diagnosisId);
		}
		
		if(patientId != null) {
			diagnoses.setPatientId(patientId);
		}
		
		return diagnoses;
	}
}
