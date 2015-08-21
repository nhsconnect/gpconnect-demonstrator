package net.nhs.esb.lab.orders.route.converter;

import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.lab.orders.model.LabOrder;
import net.nhs.esb.lab.orders.model.LabOrderUpdate;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.util.DateFormatter;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class LabOrderCompositionConverter {

    private static final String LAB_ORDER_PREFIX = "laboratory_order/laboratory_test_request/lab_request";

    @Converter
    public LabOrder convertResponseToLabOrder(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        String date = MapUtils.getString(rawComposition, LAB_ORDER_PREFIX + "/timing");
        date = DateFormatter.stripOddDate(date);

        LabOrder labOrder = new LabOrder();
        labOrder.setCompositionId(MapUtils.getString(rawComposition, "laboratory_order/_uid"));
        labOrder.setAuthor(MapUtils.getString(rawComposition, "laboratory_order/ctx/composer_name"));
        labOrder.setDate(DateFormatter.toDate(date));
        labOrder.setCode(MapUtils.getString(rawComposition, LAB_ORDER_PREFIX + "/service_requested|code"));
        labOrder.setName(MapUtils.getString(rawComposition, LAB_ORDER_PREFIX + "/service_requested|value"));
        labOrder.setSource("openehr");

        return labOrder;
    }

    @Converter
    public LabOrderUpdate convertLabOrderToUpdate(LabOrder labOrder) {

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/id_scheme", "NHS");
        content.put("ctx/id_namespace", "NHS");

        content.put(LAB_ORDER_PREFIX + "/service_requested|" + labOrder.getCode(), "true");
        content.put(LAB_ORDER_PREFIX + "/service_requested|code", labOrder.getCode());
        content.put(LAB_ORDER_PREFIX + "/service_requested|value", labOrder.getName());
        content.put(LAB_ORDER_PREFIX + "/service_requested|terminology", "SNOMED-CT");
        content.put(LAB_ORDER_PREFIX + "/timing", DateFormatter.toString(labOrder.getDate()));

        content.put("laboratory_order/laboratory_test_request/narrative", labOrder.getName());

        return new LabOrderUpdate(content);
    }
}
