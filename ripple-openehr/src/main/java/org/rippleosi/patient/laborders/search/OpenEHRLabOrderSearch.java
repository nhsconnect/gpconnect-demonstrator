package org.rippleosi.patient.laborders.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.rippleosi.patient.laborders.model.LabOrderSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRLabOrderSearch extends AbstractOpenEhrService implements LabOrderSearch {

    @Override
    public List<LabOrderSummary> findAllLabOrders(String patientId) {
        LabOrderSummaryQueryStrategy query = new LabOrderSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public LabOrderDetails findLabOrder(String patientId, String labOrderId) {
        LabOrderDetailsQueryStrategy query = new LabOrderDetailsQueryStrategy(patientId, labOrderId);

        return findData(query);
    }
}
