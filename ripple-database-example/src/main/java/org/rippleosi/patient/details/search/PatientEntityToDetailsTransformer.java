package org.rippleosi.patient.details.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.springframework.stereotype.Component;

@Component
public class PatientEntityToDetailsTransformer implements Transformer<PatientEntity, PatientDetails> {

    @Override
    public PatientDetails transform(PatientEntity patientEntity) {
        PatientDetails patient = new PatientDetails();

        String name = patientEntity.getFirstName() + " " + patientEntity.getLastName();
        String address = patientEntity.getAddress1() + ", " +
                         patientEntity.getAddress2() + ", " +
                         patientEntity.getAddress3() + ", " +
                         patientEntity.getPostcode();

        patient.setId(patientEntity.getNhsNumber());
        patient.setName(name);
        patient.setGender(patientEntity.getGender());
        patient.setDateOfBirth(patientEntity.getDateOfBirth());
        patient.setNhsNumber(patientEntity.getNhsNumber());
        patient.setPasNumber(patientEntity.getPasNumber());
        patient.setAddress(address);
        patient.setTelephone(patientEntity.getPhone());
        patient.setGpDetails(patientEntity.getGp().getName());
        patient.setPasNumber(patientEntity.getPasNumber());

        return patient;
    }
}
