package uk.gov.hscic.patient.details.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.details.model.PatientEntity;
import uk.gov.hscic.patient.details.repo.PatientRepository;
import uk.gov.hscic.patient.summary.model.PatientDetails;
import uk.gov.hscic.patient.summary.store.PatientStore;

@Service
public class LegacyPatientStore extends AbstractLegacyService implements PatientStore {

	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private PatientDetailsToEntityTransformer transformer;
	
	@Override
	public void create(PatientDetails patientDetails) {
		PatientEntity patientEntity = transformer.transform(patientDetails);
		
		patientRepository.saveAndFlush(patientEntity);
	}
}
