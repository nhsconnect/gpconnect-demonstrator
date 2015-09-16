package org.rippleosi.patient.laborders.search;

import java.util.List;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.rippleosi.patient.laborders.model.LabOrderSummary;

/**
 */
public interface LabOrderSearch extends Repository {

    List<LabOrderSummary> findAllLabOrders(String patientId);

    LabOrderDetails findLabOrder(String patientId, String labOrderId);
}
