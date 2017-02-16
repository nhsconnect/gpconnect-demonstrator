package uk.gov.hscic.patient.allergies.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.repo.AllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyAllergySearch extends AbstractLegacyService implements AllergySearch {

    @Autowired
    private AllergyRepository allergyRepository;

    // private final AllergyEntityToListTransformer transformer = new
    // AllergyEntityToListTransformer();

    @Override
    public List<AllergyListHTML> findAllAllergyHTMLTables(final String patientId) {
        System.out.println("patientId" + patientId);
        // final AllergyEntity item =
        // allergyRepository.findOne(Long.parseLong(patientId));
        List<AllergyEntity> items = allergyRepository.findAll();
        System.out.println("items " + items);

        items.get(0).getCurrentOrHistoric();

        List<AllergyListHTML> allergyList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            AllergyListHTML allergyData = new AllergyListHTML();
            allergyData.setProvider(items.get(i).getProvider());
            allergyData.setHtml(items.get(i).getHtml());
            allergyData.setCurrentOrHistoric(items.get(i).getCurrentOrHistoric());
            allergyData.setStartDate(items.get(i).getStartDate());
            allergyData.setEndDate(items.get(i).getEndDate());
            allergyData.setDetails(items.get(i).getDetails());
            
            
            allergyList.add(allergyData);
        }

        return allergyList;

        // if(items == null){
        // return null;
        // } else {
        // return Collections.singletonList(transformer.transform(items));
        // }
    }
}
