package org.rippleosi.patient.allergies.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.allergies.model.AllergyDetails;

/**
 */
public class AllergyDetailsTransformer implements Transformer<Map<String, Object>, AllergyDetails> {

    @Override
    public AllergyDetails transform(Map<String, Object> input) {

        AllergyDetails allergy = new AllergyDetails();
        allergy.setSource("openehr");
        allergy.setSourceId(MapUtils.getString(input, "uid"));
        allergy.setCause(MapUtils.getString(input, "cause"));
        allergy.setReaction(MapUtils.getString(input, "reaction"));
        allergy.setCauseCode(MapUtils.getString(input, "cause_code"));
        allergy.setCauseTerminology(MapUtils.getString(input, "cause_terminology"));

        return allergy;
    }
}
