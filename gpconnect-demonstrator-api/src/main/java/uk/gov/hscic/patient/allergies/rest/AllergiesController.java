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
package uk.gov.hscic.patient.allergies.rest;

import java.util.List;

import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.allergies.model.AllergyDetails;
import uk.gov.hscic.patient.allergies.model.AllergyHeadline;
import uk.gov.hscic.patient.allergies.model.AllergySummary;
import uk.gov.hscic.patient.allergies.search.AllergySearch;
import uk.gov.hscic.patient.allergies.search.AllergySearchFactory;
import uk.gov.hscic.patient.allergies.store.AllergyStore;
import uk.gov.hscic.patient.allergies.store.AllergyStoreFactory;
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
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final AllergySearch allergySearch = allergySearchFactory.select(sourceType);

        return allergySearch.findAllAllergies(patientId);
    }

    @RequestMapping(value = "/headlines", method = RequestMethod.GET)
    public List<AllergyHeadline> findAllergyHeadlines(@PathVariable("patientId") String patientId,
                                                      @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final AllergySearch allergySearch = allergySearchFactory.select(sourceType);

        return allergySearch.findAllergyHeadlines(patientId);
    }

    @RequestMapping(value = "/{allergyId}", method = RequestMethod.GET)
    public AllergyDetails findAllergy(@PathVariable("patientId") String patientId,
                                      @PathVariable("allergyId") String allergyId,
                                      @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final AllergySearch allergySearch = allergySearchFactory.select(sourceType);

        return allergySearch.findAllergy(patientId, allergyId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createAllergy(@PathVariable("patientId") String patientId,
                              @RequestParam(required = false) String source,
                              @RequestBody AllergyDetails allergy) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final AllergyStore allergyStore = allergyStoreFactory.select(sourceType);

        allergyStore.create(patientId, allergy);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateAllergy(@PathVariable("patientId") String patientId,
                              @RequestParam(required = false) String source,
                              @RequestBody AllergyDetails allergy) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final AllergyStore allergyStore = allergyStoreFactory.select(sourceType);

        allergyStore.update(patientId, allergy);
    }
}
