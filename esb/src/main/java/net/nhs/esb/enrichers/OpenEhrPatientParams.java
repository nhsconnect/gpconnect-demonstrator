package net.nhs.esb.enrichers;

import net.nhs.esb.rest.Patients;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component
public class OpenEhrPatientParams {
	public String[] enrich(@Header(Patients.PATIENT_ID_HEADER) Integer patientId) {
		return new String[] {"ehscape", Integer.toString(patientId)};
	}
}
