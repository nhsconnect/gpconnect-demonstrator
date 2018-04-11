package uk.gov.hscic.medications;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by jains on 04/04/2018.
 */
@RestController
@RequestMapping("api/")
public class MedicationController {

    private static final Logger LOG = Logger.getLogger("MedicationsLog");

    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private MedicationResourceProvider medicationResourceProvider;

    @GetMapping("/medication/all")
    public Map<String,List<String>> getAllMedications() {
        return medicationResourceProvider.getAllMedicationAndAllergiesForPatient();
    }
}
