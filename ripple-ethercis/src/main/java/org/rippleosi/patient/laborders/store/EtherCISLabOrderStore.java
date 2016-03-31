/*
 *   Copyright 2016 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.patient.laborders.store;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractEtherCISService;
import org.rippleosi.common.service.DefaultEtherCISStoreStrategy;
import org.rippleosi.common.service.EtherCISCreateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EtherCISLabOrderStore extends AbstractEtherCISService implements LabOrderStore {

    @Value("${etherCIS.labOrderTemplate}")
    private String labOrderTemplate;

    private static final String LAB_ORDER_PREFIX = "laboratory_order/laboratory_test_request/lab_request";

    @Override
    @Consume(uri = "activemq:Consumer.EtherCIS.VirtualTopic.EtherCIS.LabOrder.Create")
    public void create(String patientId, List<LabOrderDetails> labOrders) {

        for (LabOrderDetails labOrder : labOrders) {
            Map<String, Object> content = createFlatJsonContent(labOrder);

            EtherCISCreateStrategy createStrategy = new DefaultEtherCISStoreStrategy(patientId, labOrderTemplate, content);

            createData(createStrategy);
        }
    }

    private Map<String, Object> createFlatJsonContent(LabOrderDetails labOrder) {
        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/health_care_facility|id", "999999-345");
        content.put("ctx/health_care_facility|name", "Northumbria Community NHS");
        content.put("ctx/id_namespace", "NHS-UK");
        content.put("ctx/id_scheme", "2.16.840.1.113883.2.1.4.3");

        String author = labOrder.getAuthor() == null ? "Dr Tony Shannon" : labOrder.getAuthor();
        content.put("ctx/composer_name", author);

        Date dateToFormat = labOrder.getOrderDate() != null ? labOrder.getOrderDate() : labOrder.getDateCreated();
        String orderDate = DateFormatter.toString(dateToFormat);

        String code = labOrder.getCode() == null ? "" : labOrder.getCode();
        String terminology = labOrder.getTerminology() == null ? "SNOMED-CT" : labOrder.getTerminology();

        content.put(LAB_ORDER_PREFIX + "/timing", orderDate);
        content.put(LAB_ORDER_PREFIX + "/service_requested|code", code);
        content.put(LAB_ORDER_PREFIX + "/service_requested|value", labOrder.getName());
        content.put(LAB_ORDER_PREFIX + "/service_requested|terminology", terminology);

        content.put("laboratory_order/laboratory_test_tracker/ism_transition/careflow_step|code", "at0003");
        content.put("laboratory_order/laboratory_test_tracker/ism_transition/careflow_step|value", "Test Requested");
        content.put("laboratory_order/laboratory_test_tracker/ism_transition/current_state|code", "526");
        content.put("laboratory_order/laboratory_test_tracker/ism_transition/current_state|value", "planned");
        content.put("laboratory_order/laboratory_test_tracker/time", orderDate);

        content.put("laboratory_order/laboratory_test_tracker/test_name|terminology", terminology);
        content.put("laboratory_order/laboratory_test_tracker/test_name|code", code);
        content.put("laboratory_order/laboratory_test_tracker/test_name|value", labOrder.getName());

        content.put("laboratory_order/laboratory_test_request/narrative", labOrder.getName());

        return content;
    }
}