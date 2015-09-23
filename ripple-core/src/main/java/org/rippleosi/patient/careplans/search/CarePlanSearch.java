package org.rippleosi.patient.careplans.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.careplans.model.CarePlanDetails;
import org.rippleosi.patient.careplans.model.CarePlanSummary;

/**
 */
public interface CarePlanSearch extends Repository {

    List<CarePlanSummary> findAllCarePlans(String patientId);

    CarePlanDetails findCarePlan(String patientId, String carePlanId);
}
