package org.rippleosi.patient.laborders.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.laborders.model.LabOrderSummary;

/**
 */
public class LabOrderSummaryTransformer implements Transformer<Map<String, Object>, LabOrderSummary> {

    @Override
    public LabOrderSummary transform(Map<String, Object> input) {

        Date orderDate = DateFormatter.toDate(MapUtils.getString(input, "order_date"));

        LabOrderSummary labOrder = new LabOrderSummary();
        labOrder.setSource("openehr");
        labOrder.setSourceId(MapUtils.getString(input, "uid"));
        labOrder.setName(MapUtils.getString(input, "name"));
        labOrder.setOrderDate(orderDate);

        return labOrder;
    }
}
