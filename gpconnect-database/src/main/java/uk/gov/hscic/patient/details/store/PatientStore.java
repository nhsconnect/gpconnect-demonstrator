package uk.gov.hscic.patient.details.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.details.model.PatientEntity;
import uk.gov.hscic.patient.details.repo.PatientRepository;
import uk.gov.hscic.patient.summary.model.PatientDetails;

@Service
public class PatientStore {

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private PatientDetailsToEntityTransformer transformer;

	public void create(PatientDetails patientDetails) {
		PatientEntity patientEntity = transformer.transform(patientDetails);

		patientRepository.saveAndFlush(patientEntity);
	}
}
