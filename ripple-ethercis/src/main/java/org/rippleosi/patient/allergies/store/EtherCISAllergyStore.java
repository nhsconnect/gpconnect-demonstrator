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
package org.rippleosi.patient.allergies.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractEtherCISService;
import org.rippleosi.common.service.DefaultEtherCISStoreStrategy;
import org.rippleosi.common.service.EtherCISCreateStrategy;
import org.rippleosi.common.service.EtherCISUpdateStrategy;
import org.rippleosi.patient.allergies.model.AllergyDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class EtherCISAllergyStore extends AbstractEtherCISService implements AllergyStore {

    @Value("${etherCIS.allergyTemplate}")
    private String allergyTemplate;

    private static final String ALLERGY_PREFIX = "adverse_reaction_list/allergies_and_adverse_reactions:0/adverse_reaction_risk:0";

    @Override
    @Consume(uri = "activemq:Consumer.EtherCIS.VirtualTopic.EtherCIS.Allergies.Create")
    public void create(String patientId, AllergyDetails allergy) {

        Map<String,Object> content = createFlatJsonContent(allergy);

        EtherCISCreateStrategy createStrategy = new DefaultEtherCISStoreStrategy(patientId, allergyTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.EtherCIS.VirtualTopic.EtherCIS.Allergies.Update")
    public void update(String patientId, AllergyDetails allergy) {

        Map<String,Object> content = createFlatJsonContent(allergy);

        EtherCISUpdateStrategy updateStrategy = new DefaultEtherCISStoreStrategy(allergy.getSourceId(), patientId,
                                                                                 allergyTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String,Object> createFlatJsonContent(AllergyDetails allergy) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        String author = allergy.getAuthor();
        content.put("ctx/composer_name", author != null ? author : "Dr Tony Shannon");

        content.put(ALLERGY_PREFIX + "/causative_agent", allergy.getCause());
//        content.put(ALLERGY_PREFIX + "/causative_agent|code", allergy.getCauseCode());
//        content.put(ALLERGY_PREFIX + "/causative_agent|terminology", allergy.getCauseTerminology());
        content.put(ALLERGY_PREFIX + "/reaction_details/reaction|value", allergy.getReaction());
        content.put(ALLERGY_PREFIX + "/reaction_details/reaction|terminology", allergy.getCauseTerminology());
        content.put(ALLERGY_PREFIX + "/reaction_details/reaction|code", allergy.getCauseCode());

        return content;
    }
}
