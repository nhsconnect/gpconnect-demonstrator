package uk.gov.hscic.patient.allergies.rest;

import java.util.List;

import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.search.AllergySearch;
import uk.gov.hscic.patient.allergies.search.AllergySearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/allergies")
public class AllergiesController {

    @Autowired
    private AllergySearchFactory allergySearchFactory;

    @RequestMapping(value = "/htmlTables", method = RequestMethod.GET)
    public List<AllergyListHTML> findAllAllergyHTMLTables(@PathVariable("patientId") String patientId,
                                                          @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final AllergySearch allergySearch = allergySearchFactory.select(sourceType);

        return allergySearch.findAllAllergyHTMLTables(patientId);
    }
}
