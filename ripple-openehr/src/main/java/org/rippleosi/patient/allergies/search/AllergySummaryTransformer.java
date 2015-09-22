package org.rippleosi.patient.allergies.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.allergies.model.AllergySummary;

/**
 */
public class AllergySummaryTransformer implements Transformer<Map<String, Object>, AllergySummary> {

    @Override
    public AllergySummary transform(Map<String, Object> input) {

        AllergySummary allergy = new AllergySummary();
        allergy.setSource("openehr");
        allergy.setSourceId(MapUtils.getString(input, "uid"));
        allergy.setCause(MapUtils.getString(input, "cause"));
        allergy.setReaction(MapUtils.getString(input, "reaction"));

        return allergy;
    }
}
