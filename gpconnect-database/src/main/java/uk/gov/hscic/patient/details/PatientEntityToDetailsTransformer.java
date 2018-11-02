package uk.gov.hscic.patient.details;

import java.util.Date;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import uk.gov.hscic.model.patient.PatientDetails;
import uk.gov.hscic.practitioner.PractitionerEntity;

@Component
public class PatientEntityToDetailsTransformer implements Transformer<PatientEntity, PatientDetails> {

    @Override
    public PatientDetails transform(final PatientEntity patientEntity) {
        final PatientDetails patient = new PatientDetails();

//        Collection<String> addressList = Arrays.asList(StringUtils.trimToNull(patientEntity.getAddress1()),
//                StringUtils.trimToNull(patientEntity.getAddress2()),
//                StringUtils.trimToNull(patientEntity.getAddress3()),
//                StringUtils.trimToNull(patientEntity.getPostcode()));

        String[] address = new String[]{StringUtils.trimToNull(patientEntity.getAddress1()),
            StringUtils.trimToNull(patientEntity.getAddress2()),
            StringUtils.trimToNull(patientEntity.getAddress3()),
            StringUtils.trimToNull(patientEntity.getAddress4()),
            StringUtils.trimToNull(patientEntity.getAddress5())};

//        addressList = CollectionUtils.removeAll(addressList, Collections.singletonList(null));
//
//        final String address = StringUtils.join(addressList, ", ");
        final String name = patientEntity.getFirstName() + " " + patientEntity.getLastName();
        final String patientId = patientEntity.getNhsNumber();

        patient.setId(String.valueOf(patientEntity.getId()));
        patient.setName(name);
        patient.setTitle(patientEntity.getTitle());
        patient.setForename(patientEntity.getFirstName());
        patient.setSurname(patientEntity.getLastName());
        patient.setGender(patientEntity.getGender());
        patient.setDateOfBirth(patientEntity.getDateOfBirth());
        patient.setNhsNumber(patientId);
        patient.setPasNumber(patientEntity.getPasNumber());
        patient.setAddress(address);
        patient.setPostcode(patientEntity.getPostcode());
        patient.setTelephone(patientEntity.getPhone());
        patient.setPasNumber(patientEntity.getPasNumber());
        patient.setLastUpdated(patientEntity.getLastUpdated());
        patient.setRegistrationStartDateTime(patientEntity.getRegistrationStartDateTime());
        patient.setRegistrationEndDateTime(patientEntity.getRegistrationEndDateTime());
        patient.setRegistrationStatus(patientEntity.getRegistrationStatus());
        patient.setRegistrationType(patientEntity.getRegistrationType());
        patient.setMaritalStatus(patientEntity.getMaritalStatus());
        patient.setManagingOrganization(patientEntity.getManagingOrganization());

        PractitionerEntity gp = patientEntity.getPractitioner();

        if (gp != null) {
            patient.setGpDetails(gp.getNamePrefix() + " " + gp.getNameGiven() + " " + gp.getNameFamily());
            patient.setGpId(gp.getId());
        }

        Date deceased = patientEntity.getDeceasedDateTime();
        if (deceased != null) {
            patient.setDeceased(deceased);
        }

        patient.setSensitive(patientEntity.isSensitive());

        return patient;
    }
}
