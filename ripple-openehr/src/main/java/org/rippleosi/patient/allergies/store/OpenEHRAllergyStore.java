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

    @Value("${openehr.allergiesTemplate}")
    private String allergiesTemplate;

    private static final String ALLERGY_PREFIX = "allergies_list/allergies_and_adverse_reactions:0/adverse_reaction:0";

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.Allergies.Create")
    public void create(String patientId, AllergyDetails allergy) {

        Map<String,Object> content = createFlatJsonContent(allergy);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, allergiesTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.Allergies.Update")
    public void update(String patientId, AllergyDetails allergy) {

        Map<String,Object> content = createFlatJsonContent(allergy);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(allergy.getSourceId(), patientId, allergiesTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String,Object> createFlatJsonContent(AllergyDetails allergy) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        content.put(ALLERGY_PREFIX + "/causative_agent|value", allergy.getCause());
        content.put(ALLERGY_PREFIX + "/causative_agent|code", allergy.getCauseCode());
        content.put(ALLERGY_PREFIX + "/causative_agent|terminology", allergy.getCauseTerminology());
        content.put(ALLERGY_PREFIX + "/reaction_details/comment", allergy.getReaction());

        return content;
    }
}
