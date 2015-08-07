package net.nhs.esb.allergy.route.converter;

import java.util.List;
import java.util.Map;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class AllergyCompositionConverter extends BaseCompositionConverter<Allergy> {

    private static final String ALLERGY_UID = "allergies_list/_uid";
    private static final String ALLERGY_DEFINITION = "allergies_list/allergies_and_adverse_reactions:";

    @Converter
    public AllergyComposition convertResponseToAllergyComposition(CompositionResponseData response) {

        Map<String, Object> rawComposition = response.getComposition();

        String compositionId = MapUtils.getString(rawComposition, ALLERGY_UID);
        List<Allergy> allergyList = extractCompositionData(rawComposition);

        AllergyComposition allergyComposition = new AllergyComposition();
        allergyComposition.setCompositionId(compositionId);
        allergyComposition.setAllergies(allergyList);

        return allergyComposition;
    }

    @Override
    protected Allergy create(Map<String, Object> rawComposition, String prefix) {

        Allergy allergy = new Allergy();
        allergy.setCause(MapUtils.getString(rawComposition, prefix + "/adverse_reaction:0/causative_agent|value"));
        allergy.setReaction(MapUtils.getString(rawComposition, prefix + "/adverse_reaction:0/reaction_details/comment"));
        allergy.setCauseCode(MapUtils.getString(rawComposition, prefix + "/adverse_reaction:0/causative_agent|code"));
        allergy.setCauseTerminology(MapUtils.getString(rawComposition, prefix + "/adverse_reaction:0/causative_agent|terminology"));
        allergy.setSource("openehr");

        return allergy;
    }

    @Override
    protected String dataDefinitionPrefix() {
        return ALLERGY_DEFINITION;
    }
}
