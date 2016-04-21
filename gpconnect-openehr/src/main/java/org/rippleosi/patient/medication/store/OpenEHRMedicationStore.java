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
package org.rippleosi.patient.medication.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.apache.commons.lang3.StringUtils;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRMedicationStore extends AbstractOpenEhrService implements MedicationStore {

    @Value("${c4hOpenEHR.medicationTemplate}")
    private String medicationTemplate;

    private static final String MEDICATION_PREFIX = "current_medication_list/medication_and_medical_devices:0/current_medication:0/medication_statement:0";

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Medication.Create")
    public void create(String patientId, MedicationDetails medication) {

        Map<String, Object> content = createFlatJsonContent(medication);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, medicationTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Medication.Update")
    public void update(String patientId, MedicationDetails medication) {

        Map<String, Object> content = createFlatJsonContent(medication);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(medication.getSourceId(), patientId, medicationTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String, Object> createFlatJsonContent(MedicationDetails medication) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/composer_name", medication.getAuthor());

        String medicationCode = StringUtils.isEmpty(medication.getMedicationCode()) ? "123456789" : medication.getMedicationCode();

        String startDateTime = DateFormatter.combineDateTime(medication.getStartDate(), medication.getStartTime());

        content.put(MEDICATION_PREFIX + "/medication_item/medication_name|value", medication.getName());
        content.put(MEDICATION_PREFIX + "/medication_item/medication_name|code", medicationCode);
        content.put(MEDICATION_PREFIX + "/medication_item/medication_name|terminology", medication.getMedicationTerminology());
        content.put(MEDICATION_PREFIX + "/medication_item/route:0|code", medication.getRoute());
        content.put(MEDICATION_PREFIX + "/medication_item/route:0|value", "RouteValue");
        content.put(MEDICATION_PREFIX + "/medication_item/dose_amount_description", medication.getDoseAmount());
        content.put(MEDICATION_PREFIX + "/medication_item/dose_timing_description", medication.getDoseTiming());
        content.put(MEDICATION_PREFIX + "/medication_item/course_details/start_datetime", startDateTime);
        content.put(MEDICATION_PREFIX + "/medication_item/dose_directions_description", medication.getDoseDirections());

        return content;
    }
}
