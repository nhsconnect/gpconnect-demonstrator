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
package org.rippleosi.patient.careplans.rest;

import java.util.List;

import org.rippleosi.common.types.RepoSource;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.careplans.model.CarePlanDetails;
import org.rippleosi.patient.careplans.model.CarePlanSummary;
import org.rippleosi.patient.careplans.search.CarePlanSearch;
import org.rippleosi.patient.careplans.search.CarePlanSearchFactory;
import org.rippleosi.patient.careplans.store.CarePlanStore;
import org.rippleosi.patient.careplans.store.CarePlanStoreFactory;
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
@RequestMapping("/patients/{patientId}/eolcareplans")
public class CarePlansController {

    @Autowired
    private CarePlanSearchFactory carePlanSearchFactory;

    @Autowired
    private CarePlanStoreFactory carePlanStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<CarePlanSummary> findAllCarePlans(@PathVariable("patientId") String patientId,
                                                  @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        CarePlanSearch carePlanSearch = carePlanSearchFactory.select(sourceType);

        return carePlanSearch.findAllCarePlans(patientId);
    }

    @RequestMapping(value = "/{planId}", method = RequestMethod.GET)
    public CarePlanDetails findCarePlan(@PathVariable("patientId") String patientId,
                                        @PathVariable("planId") String planId,
                                        @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        CarePlanSearch carePlanSearch = carePlanSearchFactory.select(sourceType);

        return carePlanSearch.findCarePlan(patientId, planId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createCarePlan(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody CarePlanDetails carePlan) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        CarePlanStore carePlanStore = carePlanStoreFactory.select(sourceType);

        carePlanStore.create(patientId, carePlan);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateCarePlan(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody CarePlanDetails carePlan) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        CarePlanStore carePlanStore = carePlanStoreFactory.select(sourceType);

        carePlanStore.update(patientId, carePlan);
    }
}
