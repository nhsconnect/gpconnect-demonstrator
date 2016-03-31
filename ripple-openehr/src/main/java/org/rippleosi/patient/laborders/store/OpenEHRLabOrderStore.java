/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.laborders.store;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Value("${c4hOpenEHR.labOrderTemplate}")
    private String labOrderTemplate;

    private static final String LAB_ORDER_PREFIX = "laboratory_order/laboratory_test_request/lab_request";

    @Override
//    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.LabOrder.Create")
    public void create(String patientId, List<LabOrderDetails> labOrders) {

        for (LabOrderDetails labOrder : labOrders) {
            Map<String, Object> content = createFlatJsonContent(labOrder);

            CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, labOrderTemplate, content);

            createData(createStrategy);
        }
    }

    private Map<String, Object> createFlatJsonContent(LabOrderDetails labOrder) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        Date dateToFormat = labOrder.getOrderDate() != null ? labOrder.getOrderDate()
                                                            : labOrder.getDateCreated();
        String orderDate = DateFormatter.toString(dateToFormat);

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
