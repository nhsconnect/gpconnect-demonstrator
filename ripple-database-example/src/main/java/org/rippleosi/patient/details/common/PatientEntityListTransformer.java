package org.rippleosi.patient.details.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.springframework.stereotype.Component;

@Component
public class PatientEntityListTransformer implements Transformer<List<PatientEntity>, List<PatientSummary>> {

    @Override
    public List<PatientSummary> transform(List<PatientEntity> patientEntities) {
        if (patientEntities.size() == 0) {
            throw new DataNotFoundException("No patient data has been found");
        }

        List<PatientSummary> patientSummaries = new ArrayList<>();

        for (PatientEntity patientEntity : patientEntities) {
            PatientSummary patientSummary = new PatientSummary();

            String name = patientEntity.getFirstName() + " " + patientEntity.getLastName();
            String address = patientEntity.getAddress1() + ", " +
                             patientEntity.getAddress2() + ", " +
                             patientEntity.getAddress3() + ", " +
                             patientEntity.getPostcode();

            patientSummary.setId(patientEntity.getNhsNumber());
            patientSummary.setName(name);
            patientSummary.setAddress(address);
            patientSummary.setDateOfBirth(patientEntity.getDateOfBirth());
            patientSummary.setGender(patientEntity.getGender());
            patientSummary.setNhsNumber(patientEntity.getNhsNumber());

            patientSummaries.add(patientSummary);
        }

        return patientSummaries;
    }
}
