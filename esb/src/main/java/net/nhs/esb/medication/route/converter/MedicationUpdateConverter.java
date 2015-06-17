package net.nhs.esb.medication.route.converter;

import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.medication.model.MedicationUpdate;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class MedicationUpdateConverter {

    @Converter
    public MedicationUpdate convertCompositionToMedicationUpdate(MedicationComposition composition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        int index = 0;
        for (Medication medication : composition.getMedications()) {

            String prefix = "current_medication_list/medication_and_medical_devices:0/current_medication:0/medication_statement:" + index;

            content.put(prefix + "/medication_item/medication_name|value", medication.getName());
            content.put(prefix + "/medication_item/medication_name|code", medication.getCode());
            content.put(prefix + "/medication_item/medication_name|terminology", medication.getTerminology());
            content.put(prefix + "/medication_item/route:0|code", medication.getRoute());
            content.put(prefix + "/medication_item/route:0|value", "RouteValue");
            content.put(prefix + "/medication_item/dose_amount_description", medication.getDoseAmount());
            content.put(prefix + "/medication_item/dose_timing_description", medication.getDoseTiming());
            content.put(prefix + "/medication_item/course_details/start_datetime", medication.getStartDateTime());
            content.put(prefix + "/medication_item/dose_directions_description", medication.getDoseDirections());

            index++;
        }

        return new MedicationUpdate(content);
    }

}
