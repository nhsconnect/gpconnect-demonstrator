package org.rippleosi.patient.laborders.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.laborders.model.LabOrderDetails;

/**
 */
public class LabOrderDetailsTransformer implements Transformer<Map<String, Object>, LabOrderDetails> {

    @Override
    public LabOrderDetails transform(Map<String, Object> input) {

        Date orderDate = DateFormatter.toDate(MapUtils.getString(input, "order_date"));
        Date dateCreated = DateFormatter.toDate(MapUtils.getString(input, "date_created"));

        LabOrderDetails labOrder = new LabOrderDetails();
        labOrder.setSource("openehr");
        labOrder.setSourceId(MapUtils.getString(input, "uid"));
        labOrder.setName(MapUtils.getString(input, "name"));
        labOrder.setOrderDate(orderDate);
        labOrder.setAuthor(MapUtils.getString(input, "author"));
        labOrder.setDateCreated(dateCreated);
        labOrder.setCode(MapUtils.getString(input, "code"));
        labOrder.setTerminology(MapUtils.getString(input, "terminology"));

        return labOrder;
    }
}
