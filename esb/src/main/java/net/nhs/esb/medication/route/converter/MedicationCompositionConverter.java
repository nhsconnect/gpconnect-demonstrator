package net.nhs.esb.medication.route.converter;

import java.util.List;
import java.util.Map;

import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class MedicationCompositionConverter extends BaseCompositionConverter<Medication> {

    private static final String MEDICATION_UID = "current_medication_list/_uid";
    private static final String MEDICATION_DEFINITION = "current_medication_list/medication_and_medical_devices:0/current_medication:0/medication_statement:";

    @Converter
    public MedicationComposition convertResponseToMedicationComposition(CompositionResponseData response) {

        Map<String, Object> rawComposition = response.getComposition();

        String compositionId = MapUtils.getString(rawComposition, MEDICATION_UID);
        List<Medication> medicationList = extractCompositionData(rawComposition);

        MedicationComposition medicationComposition = new MedicationComposition();
        medicationComposition.setCompositionId(compositionId);
        medicationComposition.setMedications(medicationList);

        return medicationComposition;
    }

    @Override
    protected Medication create(Map<String, Object> rawComposition, String prefix) {

        Medication medication = new Medication();
        medication.setName(MapUtils.getString(rawComposition, prefix + "/medication_item/medication_name|value"));
        medication.setCode(MapUtils.getString(rawComposition, prefix + "/medication_item/medication_name|code"));
        medication.setTerminology(MapUtils.getString(rawComposition, prefix + "/medication_item/medication_name|terminology"));
        medication.setRoute(MapUtils.getString(rawComposition, prefix + "/medication_item/route:0|code"));
        medication.setDoseAmount(MapUtils.getString(rawComposition, prefix + "/medication_item/dose_amount_description"));
        medication.setDoseTiming(MapUtils.getString(rawComposition, prefix + "/medication_item/dose_timing_description"));
        medication.setStartDateTime(MapUtils.getString(rawComposition, prefix + "/medication_item/course_details/start_datetime"));
        medication.setDoseDirections(MapUtils.getString(rawComposition, prefix + "/medication_item/dose_directions_description"));

        return medication;
    }

    @Override
    protected String dataDefinitionPrefix() {
        return MEDICATION_DEFINITION;
    }
}
