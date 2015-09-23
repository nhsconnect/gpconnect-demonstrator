package org.rippleosi.patient.careplans.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.InOnly;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.careplans.model.CarePlanDetails;

/**
 */
@InOnly
public interface CarePlanStore extends Repository {

    void create(@Header("patientId") String patientId, @Body CarePlanDetails carePlan);

    void update(@Header("patientId") String patientId, @Body CarePlanDetails carePlan);
}
