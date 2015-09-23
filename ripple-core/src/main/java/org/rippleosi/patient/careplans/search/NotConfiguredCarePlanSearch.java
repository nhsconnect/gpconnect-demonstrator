package org.rippleosi.patient.careplans.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.careplans.model.CarePlanDetails;
import org.rippleosi.patient.careplans.model.CarePlanSummary;

/**
 */
public class NotConfiguredCarePlanSearch implements CarePlanSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<CarePlanSummary> findAllCarePlans(String patientId) {
        throw ConfigurationException.unimplementedTransaction(CarePlanSearch.class);
    }

    @Override
    public CarePlanDetails findCarePlan(String patientId, String carePlanId) {
        throw ConfigurationException.unimplementedTransaction(CarePlanSearch.class);
    }
}
