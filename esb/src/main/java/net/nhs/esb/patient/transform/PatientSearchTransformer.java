package net.nhs.esb.patient.transform;

import java.util.List;

import net.nhs.esb.patient.model.PatientDetails;
import net.nhs.esb.patient.repository.PatientRepository;
import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class PatientSearchTransformer {

    @Autowired
    private PatientRepository patientRepository;

    public PatientDetails[] findAllPatients() {

        List<PatientDetails> patientDetailsList = patientRepository.findAll();

        return patientDetailsList.toArray(new PatientDetails[patientDetailsList.size()]);
    }

    public PatientDetails findPatientById(@Header("id") Long id) {
        return patientRepository.findOne(id);
    }

    public void createPatient(PatientDetails patientDetails) {
        patientRepository.save(patientDetails);
    }

}
