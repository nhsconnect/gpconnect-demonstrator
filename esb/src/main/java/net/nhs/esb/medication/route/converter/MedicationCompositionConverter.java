package net.nhs.esb.medication.route.converter;

import java.util.List;
import java.util.Map;

import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class MedicationCompositionConverter extends BaseCompositionConverter {

    @Converter
    public MedicationComposition convertResponseToMedicationComposition(CompositionResponseData response) {

        Map<String,Object> rawComposition = getProperty(response.getComposition(), "current_medication_list");

        MedicationTransformer transformer = new MedicationTransformer();

        String compositionId = extractCompositionId(rawComposition);
        List<Medication> medicationList = extractCompositionData(rawComposition, "medication_and_medical_devices[0].current_medication[0].medication_statement", transformer);

        MedicationComposition medicationComposition = new MedicationComposition();
        medicationComposition.setCompositionId(compositionId);
        medicationComposition.setMedications(medicationList);

        return medicationComposition;
    }

    private class MedicationTransformer implements Transformer<Map<String, Object>, Medication> {

        @Override
        public Medication transform(Map<String, Object> input) {

            Map<String,Object> medicationItem = getProperty(input, "medication_item[0]");

            String name = getProperty(medicationItem, "medication_name[0].|value");
            String route = getProperty(medicationItem, "route[0].|code");
            String doseAmount = getProperty(medicationItem, "dose_amount_description[0]");
            String doseTiming = getProperty(medicationItem, "dose_timing_description[0]");
            String startDateTime = getProperty(medicationItem, "course_details[0].start_datetime[0]");
            String doseDirections = getProperty(medicationItem, "dose_directions_description[0]");

            Medication medication = new Medication();
            medication.setName(name);
            medication.setRoute(route);
            medication.setDoseAmount(doseAmount);
            medication.setDoseTiming(doseTiming);
            medication.setStartDateTime(startDateTime);
            medication.setDoseDirections(doseDirections);

            return medication;
        }
    }
}
