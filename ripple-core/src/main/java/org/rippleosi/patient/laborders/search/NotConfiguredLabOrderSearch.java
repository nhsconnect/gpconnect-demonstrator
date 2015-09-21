package org.rippleosi.patient.laborders.search;

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
        throw ConfigurationException.unimplementedTransaction(LabOrderSearch.class);
    }

    @Override
    public LabOrderDetails findLabOrder(String patientId, String labOrderId) {
        throw ConfigurationException.unimplementedTransaction(LabOrderSearch.class);
    }
}
