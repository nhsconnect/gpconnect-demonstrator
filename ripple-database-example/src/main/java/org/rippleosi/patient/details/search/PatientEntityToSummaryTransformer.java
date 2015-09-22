package org.rippleosi.patient.details.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.springframework.stereotype.Component;

@Component
public class PatientEntityToSummaryTransformer implements Transformer<PatientEntity, PatientSummary> {

    @Override
    public PatientSummary transform(PatientEntity patientEntity) {
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

        return patientSummary;
    }
}
