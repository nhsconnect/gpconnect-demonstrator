package org.rippleosi.patient.medication.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.medication.model.MedicationDetails;

/**
 */
public class MedicationDetailsTransformer implements Transformer<Map<String, Object>, MedicationDetails> {

    @Override
    public MedicationDetails transform(Map<String, Object> input) {

        String startDateTimeAsString = MapUtils.getString(input, "start_date");

        Date startDate = DateFormatter.toDateOnly(startDateTimeAsString);
        Date startTime = DateFormatter.toTimeOnly(startDateTimeAsString);

        MedicationDetails medication = new MedicationDetails();
        medication.setSource("openehr");
        medication.setSourceId(MapUtils.getString(input, "uid"));
        medication.setName(MapUtils.getString(input, "name"));
        medication.setDoseAmount(MapUtils.getString(input, "dose_amount"));
        medication.setDoseTiming(MapUtils.getString(input, "dose_timing"));
        medication.setDoseDirections(MapUtils.getString(input, "dose_directions"));
        medication.setRoute(MapUtils.getString(input, "route"));
        medication.setMedicationCode(MapUtils.getString(input, "medication_code"));
        medication.setMedicationTerminology(MapUtils.getString(input, "medication_terminology"));
        medication.setStartDate(startDate);
        medication.setStartTime(startTime);

        return medication;
    }
}
