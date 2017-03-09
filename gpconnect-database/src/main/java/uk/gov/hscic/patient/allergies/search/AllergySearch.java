package uk.gov.hscic.patient.allergies.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.allergies.model.AllergyData;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.repo.AllergyRepository;

@Service
public class AllergySearch {

    @Autowired
    private AllergyRepository allergyRepository;

    public List<AllergyData> findAllAllergyHTMLTables(final String patientId) {
        List<AllergyData> allergyList = new ArrayList<>();

        for (AllergyEntity allergyEntity : allergyRepository.findBynhsNumber(patientId)) {
            AllergyData allergyData = new AllergyData();
            allergyData.setCurrentOrHistoric(allergyEntity.getCurrentOrHistoric());
            allergyData.setStartDate(allergyEntity.getStartDate());
            allergyData.setEndDate(allergyEntity.getEndDate());
            allergyData.setDetails(allergyEntity.getDetails());
            allergyList.add(allergyData);
        }

        return allergyList;
    }
}
