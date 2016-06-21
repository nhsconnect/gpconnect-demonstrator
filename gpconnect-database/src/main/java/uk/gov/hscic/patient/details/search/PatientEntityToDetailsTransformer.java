package uk.gov.hscic.patient.details.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hscic.patient.details.model.PatientEntity;
import uk.gov.hscic.patient.summary.model.PatientDetails;

@Component
public class PatientEntityToDetailsTransformer implements Transformer<PatientEntity, PatientDetails> {

    @Override
    public PatientDetails transform(final PatientEntity patientEntity) {
        final PatientDetails patient = new PatientDetails();

        Collection<String> addressList = Arrays.asList(StringUtils.trimToNull(patientEntity.getAddress1()),
                                                       StringUtils.trimToNull(patientEntity.getAddress2()),
                                                       StringUtils.trimToNull(patientEntity.getAddress3()),
                                                       StringUtils.trimToNull(patientEntity.getPostcode()));

        addressList = CollectionUtils.removeAll(addressList, Collections.singletonList(null));

        final String address = StringUtils.join(addressList, ", ");
        final String name = patientEntity.getFirstName() + " " + patientEntity.getLastName();
        final String patientId = patientEntity.getNhsNumber();

        patient.setId(String.valueOf(patientEntity.getId()));
        patient.setName(name);
        patient.setGender(patientEntity.getGender());
        patient.setDateOfBirth(patientEntity.getDateOfBirth());
        patient.setNhsNumber(patientId);
        patient.setPasNumber(patientEntity.getPasNumber());
        patient.setAddress(address);
        patient.setTelephone(patientEntity.getPhone());
        patient.setGpDetails(patientEntity.getGp().getName());
        patient.setPasNumber(patientEntity.getPasNumber());
        patient.setGpId(patientEntity.getGp().getId());

        return patient;
    }
}
