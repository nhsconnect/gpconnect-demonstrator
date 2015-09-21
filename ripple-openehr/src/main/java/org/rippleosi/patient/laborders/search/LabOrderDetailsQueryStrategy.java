package org.rippleosi.patient.laborders.search;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.laborders.model.LabOrderDetails;

/**
 */
public class LabOrderDetailsQueryStrategy extends AbstractQueryStrategy<LabOrderDetails> {

    private final String labOrderId;

    LabOrderDetailsQueryStrategy(String patientId, String labOrderId) {
        super(patientId);
        this.labOrderId = labOrderId;
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "a_a/activities[at0001]/description/items[at0121]/value/value as name, " +
                "a_a/activities[at0001]/description/items[at0121]/value/defining_code/code_string as code, " +
                "a_a/activities[at0001]/description/items[at0121]/value/defining_code/terminology_id/value as terminology, " +
                "a_a/activities[at0001]/timing/value as order_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.referral.v0] " +
                "contains INSTRUCTION a_a[openEHR-EHR-INSTRUCTION.request-lab_test.v1] " +
                "where a/name/value='Laboratory order' " +
                "and a/uid/value='" + labOrderId + "' ";
    }

    @Override
    public LabOrderDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        Date orderDate = DateFormatter.toDate(MapUtils.getString(data, "order_date"));
        Date dateCreated = DateFormatter.toDate(MapUtils.getString(data, "date_created"));

        LabOrderDetails labOrder = new LabOrderDetails();
        labOrder.setSource("openehr");
        labOrder.setSourceId(MapUtils.getString(data, "uid"));
        labOrder.setName(MapUtils.getString(data, "name"));
        labOrder.setOrderDate(orderDate);
        labOrder.setAuthor(MapUtils.getString(data, "author"));
        labOrder.setDateCreated(dateCreated);
        labOrder.setCode(MapUtils.getString(data, "code"));
        labOrder.setTerminology(MapUtils.getString(data, "terminology"));

        return labOrder;
    }
}
