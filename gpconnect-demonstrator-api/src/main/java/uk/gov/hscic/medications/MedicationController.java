package uk.gov.hscic.medications;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.patient.allergies.AllergyEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jains on 04/04/2018.
 */
@RestController
@RequestMapping("api/")
public class MedicationController {

    private static final Logger LOG = Logger.getLogger("MedicationsLog");

    @Autowired
    private MedicationRepository medicationRepository;

    @GetMapping("/medication/all")
    public Map<String,List<String>> getAllMedications() {
        Map<String,List<String>> allergiesAssociatedWithMedicationMap = new HashMap<>();
            for (MedicationEntity medicationEntity:medicationRepository.findAll()) {
                List<AllergyEntity> allergyEntities = medicationEntity.getMedicationAllergies();
            if(allergyEntities.size() > 0) {
                for (AllergyEntity allergy :allergyEntities) {

                    allergiesAssociatedWithMedicationMap.computeIfAbsent(medicationEntity.getDisplay(), k-> new ArrayList<>()).add(allergy.getDetails());
                }
            } else {
                allergiesAssociatedWithMedicationMap.put(medicationEntity.getDisplay(), Collections.EMPTY_LIST);
            }
        }
        return allergiesAssociatedWithMedicationMap;
    }
}
