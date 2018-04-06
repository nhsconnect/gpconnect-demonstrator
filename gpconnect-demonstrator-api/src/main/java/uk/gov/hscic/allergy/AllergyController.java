package uk.gov.hscic.allergy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.patient.allergies.AllergyRepository;

import java.util.List;

/**
 * Created by jains on 06/04/2018.
 */
@RestController
@RequestMapping("api/")
public class AllergyController {
    @Autowired
    private AllergyRepository allergyRepository;

    @GetMapping("/allergy/{nhsnumber}")
    public List<String> getAllergiesByNhsNumber(@PathVariable("nhsnumber") String nhsnumber) {
        return allergyRepository.findDistinctAllergiesByNhsNumber(nhsnumber);
    }
}
