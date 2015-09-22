package org.rippleosi.patient.medication.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.medication.model.MedicationSummary;

/**
 */
public class MedicationSummaryTransformer implements Transformer<Map<String, Object>, MedicationSummary> {

    @Override
    public MedicationSummary transform(Map<String, Object> input) {

        MedicationSummary medication = new MedicationSummary();
        medication.setSource("openehr");
        medication.setSourceId(MapUtils.getString(input, "uid"));
        medication.setName(MapUtils.getString(input, "name"));
        medication.setDoseAmount(MapUtils.getString(input, "dose_amount"));

        return medication;
    }
}
