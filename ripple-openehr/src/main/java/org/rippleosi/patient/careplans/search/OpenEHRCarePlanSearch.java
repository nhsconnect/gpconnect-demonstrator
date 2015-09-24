package org.rippleosi.patient.careplans.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.careplans.model.CarePlanDetails;
import org.rippleosi.patient.careplans.model.CarePlanSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRCarePlanSearch extends AbstractOpenEhrService implements CarePlanSearch {

    @Override
    public List<CarePlanSummary> findAllCarePlans(String patientId) {
        CarePlanSummaryQueryStrategy query = new CarePlanSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public CarePlanDetails findCarePlan(String patientId, String carePlanId) {
        CarePlanDetailsQueryStrategy query = new CarePlanDetailsQueryStrategy(patientId, carePlanId);

        return findData(query);
    }
}
