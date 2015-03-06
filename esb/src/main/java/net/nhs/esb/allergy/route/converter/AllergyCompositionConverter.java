package net.nhs.esb.allergy.route.converter;

import java.util.List;
import java.util.Map;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class AllergyCompositionConverter extends BaseCompositionConverter {

    @Converter
    public AllergyComposition convertResponseToAllergyComposition(CompositionResponseData response) {

        Map<String,Object> rawComposition = getProperty(response.getComposition(), "allergies_list");

        AllergyTransformer transformer = new AllergyTransformer();

        String compositionId = extractCompositionId(rawComposition);
        List<Allergy> allergyList = extractCompositionData(rawComposition, "allergies_and_adverse_reactions", transformer);

        AllergyComposition allergyComposition = new AllergyComposition();
        allergyComposition.setCompositionId(compositionId);
        allergyComposition.setAllergies(allergyList);

        return allergyComposition;
    }

    private class AllergyTransformer implements Transformer<Map<String,Object>, Allergy> {

        @Override
        public Allergy transform(Map<String, Object> input) {

            Map<String,Object> adverseReaction = getProperty(input, "adverse_reaction[0]");

            String cause = getProperty(adverseReaction, "causative_agent[0].|value");
            String reaction = getProperty(adverseReaction, "reaction_details[0].comment[0]");

            Allergy allergy = new Allergy();
            allergy.setCause(cause);
            allergy.setReaction(reaction);

            return allergy;
        }
    }
}
