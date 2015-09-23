package org.rippleosi.patient.careplans.rest;

import java.util.List;

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
public class CarePlanController {

    @Autowired
    private CarePlanSearchFactory carePlanSearchFactory;

    @Autowired
    private CarePlanStoreFactory carePlanStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<CarePlanSummary> findAllCarePlans(@PathVariable("patientId") String patientId,
                                                  @RequestParam(required = false) String source) {
        CarePlanSearch carePlanSearch = carePlanSearchFactory.select(source);

        return carePlanSearch.findAllCarePlans(patientId);
    }

    @RequestMapping(value = "/{planId}", method = RequestMethod.GET)
    public CarePlanDetails findCarePlan(@PathVariable("patientId") String patientId,
                                        @PathVariable("planId") String planId,
                                        @RequestParam(required = false) String source) {
        CarePlanSearch carePlanSearch = carePlanSearchFactory.select(source);

        return carePlanSearch.findCarePlan(patientId, planId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createCarePlan(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody CarePlanDetails carePlan) {
        CarePlanStore carePlanStore = carePlanStoreFactory.select(source);

        carePlanStore.create(patientId, carePlan);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateCarePlan(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody CarePlanDetails carePlan) {
        CarePlanStore carePlanStore = carePlanStoreFactory.select(source);

        carePlanStore.update(patientId, carePlan);
    }
}
