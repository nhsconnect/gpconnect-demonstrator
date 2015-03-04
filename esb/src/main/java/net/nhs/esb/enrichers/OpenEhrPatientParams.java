package net.nhs.esb.enrichers;

import net.nhs.esb.rest.Patients;
import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenEhrPatientParams {

    @Value("${openehr.subjectNamespace}")
    private String subjectNamespace;

    public String[] enrich(@Header(Patients.PATIENT_ID_HEADER) Long patientId) {
        return new String[]{ subjectNamespace, String.valueOf(patientId) };
    }
}
