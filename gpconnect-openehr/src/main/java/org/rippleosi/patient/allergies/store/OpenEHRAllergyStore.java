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
package org.rippleosi.patient.allergies.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.patient.allergies.model.AllergyDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRAllergyStore extends AbstractOpenEhrService implements AllergyStore {

    @Value("${c4hOpenEHR.allergiesTemplate}")
    private String allergiesTemplate;

    private static final String ALLERGY_PREFIX = "allergies_list/allergies_and_adverse_reactions:0/adverse_reaction:0";

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Allergies.Create")
    public void create(String patientId, AllergyDetails allergy) {

        Map<String,Object> content = createFlatJsonContent(allergy);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, allergiesTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Allergies.Update")
    public void update(String patientId, AllergyDetails allergy) {

        Map<String,Object> content = createFlatJsonContent(allergy);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(allergy.getSourceId(), patientId, allergiesTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String,Object> createFlatJsonContent(AllergyDetails allergy) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/composer_name", allergy.getAuthor());

        content.put(ALLERGY_PREFIX + "/causative_agent|value", allergy.getCause());
        content.put(ALLERGY_PREFIX + "/causative_agent|code", allergy.getCauseCode());
        content.put(ALLERGY_PREFIX + "/causative_agent|terminology", allergy.getCauseTerminology());
        content.put(ALLERGY_PREFIX + "/reaction_details/comment", allergy.getReaction());

        return content;
    }
}
