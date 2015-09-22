package org.rippleosi.patient.allergies.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.allergies.model.AllergyHeadline;

/**
 */
public class AllergyHeadlineTransformer implements Transformer<Map<String, Object>, AllergyHeadline> {

    @Override
    public AllergyHeadline transform(Map<String, Object> input) {

        AllergyHeadline allergy = new AllergyHeadline();
        allergy.setSource("openehr");
        allergy.setSourceId(MapUtils.getString(input, "uid"));
        allergy.setCause(MapUtils.getString(input, "cause"));

        return allergy;
    }
}
