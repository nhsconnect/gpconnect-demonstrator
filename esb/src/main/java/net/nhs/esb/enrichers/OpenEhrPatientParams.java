package net.nhs.esb.enrichers;

import net.nhs.esb.rest.Patients;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component
public class OpenEhrPatientParams {
    public String[] enrich(@Header(Patients.PATIENT_ID_HEADER) Long patientId) {
        return new String[]{ "ehrscape", String.valueOf(patientId) };
    }
}
