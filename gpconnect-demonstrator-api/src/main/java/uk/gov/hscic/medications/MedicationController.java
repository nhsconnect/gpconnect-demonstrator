package uk.gov.hscic.medications;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.LinkedHashMap;
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
    private MedicationResourceProvider medicationResourceProvider;

    @GetMapping("/medication/all")
    public Map<String,List<String>> getAllMedications() {
        return medicationResourceProvider.getAllMedicationAndAllergiesForPatient();
    }

    @PutMapping("/medication/add")
    public void addMedication(@RequestBody LinkedHashMap data) throws ParseException {
        System.out.println(data);
        medicationResourceProvider.addMedicationStatement(data);
    }
}
