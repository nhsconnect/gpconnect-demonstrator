package uk.gov.hscic.patient.emergencycodes.search;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.emergencycodes.model.EmergencyCodeEntity;
import uk.gov.hscic.patient.emergencycodes.repo.EmergencyCodeRepository;

@Service
public class EmergencyCodeSearch {

    @Autowired
    private EmergencyCodeRepository emergencyCodeRepository;

    public List<EmergencyCodeEntity> findEmergencyCodes(final String patientId) {
        return emergencyCodeRepository.findBynhsNumberOrderByEmergencyCodeDateDesc(patientId);
    }
}
