package org.rippleosi.patient.laborders.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRLabOrderStore extends AbstractOpenEhrService implements LabOrderStore {

    @Value("${openehr.labOrderTemplate}")
    private String labOrderTemplate;

    private static final String LAB_ORDER_PREFIX = "laboratory_order/laboratory_test_request/lab_request";

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.LabOrder.Create")
    public void create(String patientId, LabOrderDetails labOrder) {

        Map<String, Object> content = createFlatJsonContent(labOrder);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, labOrderTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.LabOrder.Update")
    public void update(String patientId, LabOrderDetails labOrder) {

        Map<String, Object> content = createFlatJsonContent(labOrder);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(labOrder.getSourceId(), patientId, labOrderTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String, Object> createFlatJsonContent(LabOrderDetails labOrder) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        String orderDate = DateFormatter.toString(labOrder.getOrderDate());
        String code = labOrder.getCode() == null ? "" : labOrder.getCode();
        String terminology = labOrder.getTerminology() == null ? "SNOMED-CT" : labOrder.getTerminology();

        content.put(LAB_ORDER_PREFIX + "/service_requested|" + code, "true");
        content.put(LAB_ORDER_PREFIX + "/service_requested|code", code);
        content.put(LAB_ORDER_PREFIX + "/service_requested|value", labOrder.getName());
        content.put(LAB_ORDER_PREFIX + "/service_requested|terminology", terminology);
        content.put(LAB_ORDER_PREFIX + "/timing", orderDate);

        content.put("laboratory_order/laboratory_test_request/narrative", labOrder.getName());

        return content;
    }
}
