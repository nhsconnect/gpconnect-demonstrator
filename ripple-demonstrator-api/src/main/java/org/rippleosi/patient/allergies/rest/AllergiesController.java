package org.rippleosi.patient.allergies.rest;

import java.util.List;

import org.rippleosi.patient.allergies.model.AllergyDetails;
import org.rippleosi.patient.allergies.model.AllergyHeadline;
import org.rippleosi.patient.allergies.model.AllergySummary;
import org.rippleosi.patient.allergies.search.AllergySearch;
import org.rippleosi.patient.allergies.search.AllergySearchFactory;
import org.rippleosi.patient.allergies.store.AllergyStore;
import org.rippleosi.patient.allergies.store.AllergyStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/allergies/{patientId}/allergies")
public class AllergiesController {

    @Autowired
    private AllergySearchFactory allergySearchFactory;

    @Autowired
    private AllergyStoreFactory allergyStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<AllergySummary> findAllAllergies(@PathVariable("patientId") String patientId,
                                                 @RequestParam(required = false) String source) {
        AllergySearch allergySearch = allergySearchFactory.select(source);

        return allergySearch.findAllAllergies(patientId);
    }

    @RequestMapping(value = "/headlines", method = RequestMethod.GET)
    public List<AllergyHeadline> findAllergyHeadlines(@PathVariable("patientId") String patientId,
                                                      @RequestParam(required = false) String source) {
        AllergySearch allergySearch = allergySearchFactory.select(source);

        return allergySearch.findAllergyHeadlines(patientId);
    }

    @RequestMapping(value = "/{allergyId}", method = RequestMethod.GET)
    public AllergyDetails findAllergy(@PathVariable("patientId") String patientId,
                                      @PathVariable("allergyId") String allergyId,
                                      @RequestParam(required = false) String source) {
        AllergySearch allergySearch = allergySearchFactory.select(source);

        return allergySearch.findAllergy(patientId, allergyId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createPatientAllergy(@PathVariable("patientId") String patientId,
                                     @RequestParam(required = false) String source,
                                     @RequestBody AllergyDetails allergy) {

        AllergyStore allergyStore = allergyStoreFactory.select(source);

        allergyStore.create(patientId, allergy);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updatePatientAllergy(@PathVariable("patientId") String patientId,
                                     @RequestParam(required = false) String source,
                                     @RequestBody AllergyDetails allergy) {

        AllergyStore allergyStore = allergyStoreFactory.select(source);

        allergyStore.update(patientId, allergy);
    }
}
