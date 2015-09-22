package org.rippleosi.patient.medication.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.medication.model.MedicationHeadline;

/**
 */
public class MedicationHeadlineTransformer implements Transformer<Map<String, Object>, MedicationHeadline> {

    @Override
    public MedicationHeadline transform(Map<String, Object> input) {

        MedicationHeadline medication = new MedicationHeadline();
        medication.setSource("openehr");
        medication.setSourceId(MapUtils.getString(input, "uid"));
        medication.setName(MapUtils.getString(input, "name"));

        return medication;
    }
}
