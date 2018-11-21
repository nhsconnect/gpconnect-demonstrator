package uk.gov.hscic.patient.details;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.hscic.model.patient.PatientDetails;
import uk.gov.hscic.model.telecom.TelecomDetails;
import uk.gov.hscic.practitioner.PractitionerEntity;
import uk.gov.hscic.practitioner.PractitionerRepository;
import uk.gov.hscic.telecom.TelecomEntity;

@Component
public class PatientDetailsToEntityTransformer implements Transformer<PatientDetails, PatientEntity> {

	@Autowired
	private PractitionerRepository practitionerRepository;

	@Override
	public PatientEntity transform(PatientDetails patientDetails) {
		PatientEntity patientEntity = null;

		if (patientDetails != null) {
			patientEntity = new PatientEntity();
			String[] address = patientDetails.getAddress();

 			if (address != null) {
//				final String[] addressLines = StringUtils.split(address, ",");
//
//				if (addressLines.length > 0) {
//					patientEntity.setAddress1(addressLines[0]);
//
//					if (addressLines.length > 1) {
//						patientEntity.setAddress2(addressLines[1]);
//
//						if (addressLines.length > 2) {
//							patientEntity.setAddress3(addressLines[2]);
//
//							if (addressLines.length > 3) {
//								patientEntity.setPostcode(addressLines[3]);
//							}
//						}
//					}
//				}
                patientEntity.setAddress1(address[0]);
                patientEntity.setAddress2(address[1]);
                patientEntity.setAddress3(address[2]);
                patientEntity.setAddress4(address[3]);
                patientEntity.setAddress5(address[4]);
			}
            
            patientEntity.setPostcode(patientDetails.getPostcode());

			if (patientDetails.getGpDetails() != null) {
				List<PractitionerEntity> gpList = practitionerRepository.findByUserId(patientDetails.getGpDetails());

				if (gpList.size() == 1) {
					patientEntity.setPractitioner(gpList.get(0));
				} else if (gpList.size() > 1) {
					throw new IllegalStateException(String.format("Invalid number of GPs associated with patient - (first name - %s, last name - %s). Expected - 1. Actual - %d", patientDetails.getForename(), patientDetails.getSurname(), gpList.size()));
				}
			}

			 Date deceased = patientEntity.getDeceasedDateTime();
		        if(deceased != null) {
		            patientEntity.setDeceasedDateTime(deceased);
		        }

			patientEntity.setNhsNumber(patientDetails.getNhsNumber());
			patientEntity.setDateOfBirth(patientDetails.getDateOfBirth());
			patientEntity.setFirstName(patientDetails.getForename());
			patientEntity.setGender(patientDetails.getGender());
			patientEntity.setId(patientDetails.getId() != null ? Long.parseLong(patientDetails.getId()) : null);
			patientEntity.setLastName(patientDetails.getSurname());
			patientEntity.setLastUpdated(patientDetails.getLastUpdated());
			patientEntity.setNhsNumber(patientDetails.getNhsNumber());
			patientEntity.setPasNumber(patientDetails.getPasNumber());
			patientEntity.setPhone(patientDetails.getTelephone());
			patientEntity.setTitle(patientDetails.getTitle());
			patientEntity.setRegistrationStartDateTime(patientDetails.getRegistrationStartDateTime());
			patientEntity.setRegistrationEndDateTime(patientDetails.getRegistrationEndDateTime());
			patientEntity.setRegistrationStatus(patientDetails.getRegistrationStatus());
			patientEntity.setRegistrationType(patientDetails.getRegistrationType());
			patientEntity.setMaritalStatus(patientDetails.getMaritalStatus());
			patientEntity.setManagingOrganization(patientDetails.getManagingOrganization());
			patientEntity.setSensitive(patientDetails.isSensitive());
			populateTelecoms(patientDetails, patientEntity);
		}

		return patientEntity;
	}

    /**
     * details to entity
     * there must be a better way ..
     * @param patientDetails
     * @param patientEntity 
     */
    private void populateTelecoms(PatientDetails patientDetails, PatientEntity patientEntity) {
        ArrayList<TelecomEntity> al = new ArrayList<>();
        if (patientDetails.getTelecoms() != null) {
            for (TelecomDetails telecomDetails : patientDetails.getTelecoms()){
                TelecomEntity telecomEntity = new TelecomEntity();
                // this has to be populated even though its wrong and get ovewritten with the correct value later
                telecomEntity.setPatientId(1L);
                telecomEntity.setSystem(telecomDetails.getSystem());
                telecomEntity.setUseType(telecomDetails.getUseType());
                telecomEntity.setValue(telecomDetails.getValue());
                al.add(telecomEntity);
            }
        }
        patientEntity.setTelecoms(al);
    }
}
