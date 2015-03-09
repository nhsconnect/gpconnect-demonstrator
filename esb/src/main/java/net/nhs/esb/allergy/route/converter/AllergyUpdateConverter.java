package net.nhs.esb.allergy.route.converter;

import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.allergy.model.AllergyUpdate;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class AllergyUpdateConverter {

    @Converter
    public AllergyUpdate convertCompositionToAllergyUpdate(AllergyComposition composition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        int index = 0;
        for (Allergy allergy : composition.getAllergies()) {

            String prefix = "allergies_list/allergies_and_adverse_reactions:" + index + "/adverse_reaction:0";

            content.put(prefix + "/causative_agent|value", allergy.getCause());
            content.put(prefix + "/reaction_details/comment", allergy.getReaction());
            content.put(prefix + "/causative_agent|code", allergy.getCauseCode());
            content.put(prefix + "/causative_agent|terminology", allergy.getCauseTerminology());

            index++;
        }

        return new AllergyUpdate(content);
    }

}
