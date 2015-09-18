package org.rippleosi.patient.details.search;

import java.util.List;

import org.rippleosi.patient.details.common.PatientEntityListTransformer;
import org.rippleosi.patient.details.common.PatientEntityTransformer;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.details.repo.PatientRepository;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyPatientSearch implements PatientSearch {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientEntityTransformer patientEntityTransformer;

    @Autowired
    private PatientEntityListTransformer patientEntityListTransformer;

    @Override
    public List<PatientSummary> findAllPatients() {
        List<PatientEntity> patients = patientRepository.findAll();
        return patientEntityListTransformer.transform(patients);
    }

    @Override
    public PatientDetails findPatient(String patientId) {
        PatientEntity patient = patientRepository.findByPatientId(patientId);
        return patientEntityTransformer.transform(patient);
    }

    @Override
    public String getSource() {
        return "legacy";
    }

    @Override
    public int getPriority() {
        return 900;
    }
}
