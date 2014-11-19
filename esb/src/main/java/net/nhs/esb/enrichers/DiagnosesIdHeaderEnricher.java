package net.nhs.esb.enrichers;

import java.util.Map;

import net.nhs.domain.openehr.constants.HeaderConstants;
import net.nhs.domain.openehr.model.DataSourceId;
import net.nhs.esb.rest.Patients;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

@Component
public class DiagnosesIdHeaderEnricher {
	public Object enrich(@Body Object object, @Header(Patients.DIAGNOSIS_ID_HEADER) String diagnosisId, @Headers Map<String, Object> headers) {
		String[] diagnosisIdData = diagnosisId.split("::");
		headers.put(HeaderConstants.DIAGNOSIS_ENTITY_ID, diagnosisIdData[0]);
		headers.put(HeaderConstants.DATA_SOURCE_ID, DataSourceId.fromString(diagnosisIdData[1]));
		return object;
	}
}
