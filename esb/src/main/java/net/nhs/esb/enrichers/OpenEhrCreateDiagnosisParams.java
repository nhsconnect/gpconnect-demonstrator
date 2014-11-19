package net.nhs.esb.enrichers;

import net.nhs.esb.rest.domain.EhrDiagnosisRequest;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component
public class OpenEhrCreateDiagnosisParams {
	public Object[] enrich(@Header("ehrDiagnosisRequest") EhrDiagnosisRequest ehrDiagnosisRequest, @Header("ehrId") String ehrId) {
		return new Object[] {ehrDiagnosisRequest, "Patient Diagnosis (composition)", ehrId, "STRUCTURED"};
	}
}
