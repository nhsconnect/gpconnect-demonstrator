package uk.gov.hscic.medications;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<MedicationEntity> getAllMedications() {
        return medicationRepository.findAll();
    }
}
