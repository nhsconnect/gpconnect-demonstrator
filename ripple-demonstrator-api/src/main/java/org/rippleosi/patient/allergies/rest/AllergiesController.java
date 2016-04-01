/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
@RequestMapping("/patients/{patientId}/allergies")
public class AllergiesController {

    @Autowired
    private AllergySearchFactory allergySearchFactory;

    @Autowired
    private AllergyStoreFactory allergyStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<AllergySummary> findAllAllergies(@PathVariable("patientId") String patientId,
                                                 @RequestParam(required = false) String source) {
        AllergySearch ethercisSearch = allergySearchFactory.select(source);
        List<AllergySummary> allergies = ethercisSearch.findAllAllergies(patientId);

        AllergySearch openehrSearch = allergySearchFactory.select("Marand");
        allergies.addAll(openehrSearch.findAllAllergies(patientId));

        return allergies;
    }

    @RequestMapping(value = "/headlines", method = RequestMethod.GET)
    public List<AllergyHeadline> findAllergyHeadlines(@PathVariable("patientId") String patientId,
                                                      @RequestParam(required = false) String source) {
        AllergySearch ethercisSearch = allergySearchFactory.select(source);
        List<AllergyHeadline> allergies = ethercisSearch.findAllergyHeadlines(patientId);

        AllergySearch openehrSearch = allergySearchFactory.select("Marand");
        allergies.addAll(openehrSearch.findAllergyHeadlines(patientId));

        return allergies;
    }

    @RequestMapping(value = "/{allergyId}", method = RequestMethod.GET)
    public AllergyDetails findAllergy(@PathVariable("patientId") String patientId,
                                      @PathVariable("allergyId") String allergyId,
                                      @RequestParam(required = false) String source) {
        AllergySearch allergySearch = allergySearchFactory.select(source);

        return allergySearch.findAllergy(patientId, allergyId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createAllergy(@PathVariable("patientId") String patientId,
                              @RequestParam(required = false) String source,
                              @RequestBody AllergyDetails allergy) {
        AllergyStore allergyStore = allergyStoreFactory.select("EtherCIS");

        allergyStore.create(patientId, allergy);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateAllergy(@PathVariable("patientId") String patientId,
                              @RequestParam(required = false) String source,
                              @RequestBody AllergyDetails allergy) {
        AllergyStore allergyStore = allergyStoreFactory.select(source);

        allergyStore.update(patientId, allergy);
    }
}
