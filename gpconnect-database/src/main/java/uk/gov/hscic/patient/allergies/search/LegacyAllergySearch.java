package uk.gov.hscic.patient.allergies.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.model.AllergyData;
import uk.gov.hscic.patient.allergies.repo.AllergyRepository;

@Service
public class LegacyAllergySearch extends AbstractLegacyService implements AllergySearch {

    @Autowired
    private AllergyRepository allergyRepository;

    @Override
    public List<AllergyData> findAllAllergyHTMLTables(final String patientId) {

        List<AllergyEntity> items = allergyRepository.findBynhsNumber(patientId);

        List<AllergyData> allergyList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            AllergyData allergyData = new AllergyData();
            allergyData.setCurrentOrHistoric(items.get(i).getCurrentOrHistoric());
            allergyData.setStartDate(items.get(i).getStartDate());
            allergyData.setEndDate(items.get(i).getEndDate());
            allergyData.setDetails(items.get(i).getDetails());
            allergyList.add(allergyData);
        }

        return allergyList;

    }
}
