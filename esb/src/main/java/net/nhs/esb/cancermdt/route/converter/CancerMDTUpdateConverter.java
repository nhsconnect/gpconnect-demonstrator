package net.nhs.esb.cancermdt.route.converter;

import java.util.HashMap;
import java.util.Map;
import net.nhs.esb.cancermdt.model.CancerMDT;
import net.nhs.esb.cancermdt.model.CancerMDTComposition;
import net.nhs.esb.cancermdt.model.CancerMDTUpdate;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class CancerMDTUpdateConverter {

    @Converter
    public CancerMDTUpdate convertCompositionToCancerMDTUpdate(CancerMDTComposition composition) {

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        int index = 0;
        CancerMDT cancerMDT = composition.getCancerMDT();

        /*
        String prefix = "current_medication_list/medication_and_medical_devices:0/current_medication:0/medication_statement:" + index;
        content.put(prefix + "/medication_item/medication_name|value", medication.getName());
        content.put(prefix + "/medication_item/medication_name|code", medication.getCode());
        content.put(prefix + "/medication_item/medication_name|terminology", medication.getTerminology());
        content.put(prefix + "/medication_item/route:0|code", medication.getRoute());
        content.put(prefix + "/medication_item/dose_amount_description", medication.getDoseAmount());
        content.put(prefix + "/medication_item/dose_timing_description", medication.getDoseTiming());
        content.put(prefix + "/medication_item/course_details/start_datetime", medication.getStartDateTime());
        content.put(prefix + "/medication_item/dose_directions_description", medication.getDoseDirections());
        */
        
        return new CancerMDTUpdate(content);
    }

}
