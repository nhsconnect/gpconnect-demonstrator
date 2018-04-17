package uk.gov.hscic.medications;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hscic.medication.statement.MedicationStatementEntity;

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

    @GetMapping("/medication/all//{nhsnumber}")
    public Map<String,List<String>> getAllMedications(@PathVariable("nhsnumber") String nhsnumber) {
        return medicationResourceProvider.getAllMedicationAndAllergiesForPatient(nhsnumber);
    }

    @PutMapping("/medication/add")
    public MedicationStatementEntity addMedication(@RequestBody LinkedHashMap data) throws ParseException {
        return medicationResourceProvider.addMedicationStatement(data);
    }
}
