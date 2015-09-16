package org.rippleosi.patient.laborders.search;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.rippleosi.patient.laborders.model.LabOrderSummary;

/**
 */
public class NotConfiguredLabOrderSearch implements LabOrderSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<LabOrderSummary> findAllLabOrders(String patientId) {
        // TODO Replace this code with notImplemented()

        LabOrderSummary labOrder = new LabOrderSummary();
        labOrder.setSourceId("1");
        labOrder.setSource("openehr");
        labOrder.setName("Blood Glucose");
        labOrder.setOrderDate(new Date());

        return Collections.singletonList(labOrder);
    }

    @Override
    public LabOrderDetails findLabOrder(String patientId, String labOrderId) {
        LabOrderDetails labOrder = new LabOrderDetails();
        labOrder.setSourceId("1");
        labOrder.setSource("openehr");
        labOrder.setName("Blood Glucose");
        labOrder.setOrderDate(new Date());
        labOrder.setAuthor("Dr John Smith");

        return labOrder;
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + LabOrderSearch.class.getSimpleName() + " instance");
    }
}
