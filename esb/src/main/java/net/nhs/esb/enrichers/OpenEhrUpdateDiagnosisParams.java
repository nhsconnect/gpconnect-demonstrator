package net.nhs.esb.enrichers;

import net.nhs.esb.rest.Patients;
import net.nhs.esb.rest.domain.EhrDiagnosisRequest;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component
public class OpenEhrUpdateDiagnosisParams {
	public Object[] enrich(@Header("ehrDiagnosisRequest") EhrDiagnosisRequest ehrDiagnosisRequest, @Header(Patients.DIAGNOSIS_ID_HEADER) String diagnosisId, @Header("ehrId") String ehrId) {
		return new Object[] {ehrDiagnosisRequest, diagnosisId, "Patient Diagnosis (composition)", ehrId, "STRUCTURED"};
	}
}
