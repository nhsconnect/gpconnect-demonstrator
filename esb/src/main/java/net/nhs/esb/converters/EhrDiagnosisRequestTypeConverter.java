package net.nhs.esb.converters;

import java.util.Arrays;

import net.nhs.domain.openehr.model.Diagnoses;
import net.nhs.esb.rest.domain.EhrDiagnosis;
import net.nhs.esb.rest.domain.EhrDiagnosisRequest;
import net.nhs.esb.rest.domain.EhrProblemDiagnosis;

import org.apache.camel.Converter;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Component;

@Converter
@Component
public class EhrDiagnosisRequestTypeConverter {
	FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
	
	@Converter
	public EhrDiagnosisRequest convert(Diagnoses diagnoses) {
		EhrProblemDiagnosis ehrProblemDiagnosis = new EhrProblemDiagnosis();
		if(diagnoses.getDateOfOnset() != null) {
			ehrProblemDiagnosis.setDateOfOnset(Arrays.asList(formatter.format(diagnoses.getDateOfOnset())));
		}
		
		if(diagnoses.getDescription() != null) {
			ehrProblemDiagnosis.setDescription(Arrays.asList(diagnoses.getDescription()));
		}
		
		if(diagnoses.getProblemDiagnosis() != null) {
			ehrProblemDiagnosis.setProblemDiagnosis(Arrays.asList(diagnoses.getProblemDiagnosis()));
		}
		
		EhrDiagnosis ehrDiagnosis = new EhrDiagnosis();
		ehrDiagnosis.setProblemDiagnosis(Arrays.asList(ehrProblemDiagnosis));
		
		EhrDiagnosisRequest ehrDiagnosisRequest = new EhrDiagnosisRequest();
    	ehrDiagnosisRequest.setContextLanguage("en");
    	ehrDiagnosisRequest.setContextTerritory("US");
    	ehrDiagnosisRequest.setDiagnosis(ehrDiagnosis);
    	
    	return ehrDiagnosisRequest;
	}
}
