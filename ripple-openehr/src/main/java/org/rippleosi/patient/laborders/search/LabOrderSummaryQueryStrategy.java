package org.rippleosi.patient.laborders.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.laborders.model.LabOrderSummary;

/**
 */
public class LabOrderSummaryQueryStrategy extends AbstractListQueryStrategy<LabOrderSummary> {

    LabOrderSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a_a/activities[at0001]/description/items[at0121]/value/value as name, " +
                "a_a/activities[at0001]/timing/value as order_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.referral.v0] " +
                "contains INSTRUCTION a_a[openEHR-EHR-INSTRUCTION.request-lab_test.v1] " +
                "where a/name/value='Laboratory order' ";
    }

    @Override
    public List<LabOrderSummary> transform(List<Map<String, Object>> resultSet) {

        List<LabOrderSummary> labOrderList = new ArrayList<>();

        for (Map<String, Object> data : resultSet) {

            Date orderDate = DateFormatter.toDate(MapUtils.getString(data, "order_date"));

            LabOrderSummary labOrder = new LabOrderSummary();
            labOrder.setSource("openehr");
            labOrder.setSourceId(MapUtils.getString(data, "uid"));
            labOrder.setName(MapUtils.getString(data, "name"));
            labOrder.setOrderDate(orderDate);

            labOrderList.add(labOrder);
        }

        return labOrderList;
    }
}
