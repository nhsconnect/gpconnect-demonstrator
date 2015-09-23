package org.rippleosi.patient.careplans.store;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.careplans.model.CarePlanDetails;

/**
 */
public class NotConfiguredCarePlanStore implements CarePlanStore {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void create(@Header("patientId") String patientId, @Body CarePlanDetails carePlan) {
        throw ConfigurationException.unimplementedTransaction(CarePlanStore.class);
    }

    @Override
    public void update(@Header("patientId") String patientId, @Body CarePlanDetails carePlan) {
        throw ConfigurationException.unimplementedTransaction(CarePlanStore.class);
    }
}
