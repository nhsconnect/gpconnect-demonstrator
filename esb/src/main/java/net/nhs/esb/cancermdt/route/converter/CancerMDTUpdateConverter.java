package net.nhs.esb.cancermdt.route.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

        List<CancerMDT> cancerMDTs = composition.getCancerMDT();
        Map<String, Object> rawComposition = null;

        int index = 0;

        SimpleDateFormat openEHRDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentDateTime = openEHRDateFormat.format(Calendar.getInstance().getTime());
            
        for (CancerMDT cancerMDT : cancerMDTs) {

            if (rawComposition == null) {
                rawComposition = cancerMDT.getRawComposition();
            }

            String prefix = "cancer_mdt_output_report/referral_details:" + index;
            
            // Add the updated or created fields passed from web page
            rawComposition.put(prefix + "/original_referral/request:0/service_requested", cancerMDT.getService());
            rawComposition.put("cancer_mdt_output_report/plan_and_requested_actions:" + index + "/recommendation:0/recommendation", cancerMDT.getNotes());

            
            // Set defailt values for fields requred in openEHR validation
            if(rawComposition.get(prefix + "/mdt_referral/request:0/service_requested") == null){
                rawComposition.put(prefix + "/mdt_referral/request:0/service_requested", "MDT referral");
            }

            if(rawComposition.get(prefix + "/mdt_referral/request:0/date_or_time_service_required") == null){
                rawComposition.put(prefix + "/mdt_referral/request:0/date_or_time_service_required", currentDateTime);
            }
            if(rawComposition.get(prefix + "/mdt_referral/request:0/timing") == null){
                rawComposition.put(prefix + "/mdt_referral/request:0/timing", currentDateTime);
            }
            if(rawComposition.get(prefix + "/original_referral/request:0/timing") == null){
                rawComposition.put(prefix + "/original_referral/request:0/timing", currentDateTime);
            }
            if(rawComposition.get(prefix + "/mdt_referral/narrative") == null){
                rawComposition.put(prefix + "/mdt_referral/narrative", "Default Narrative");
            }
            if(rawComposition.get(prefix + "/original_referral/narrative") == null){
                rawComposition.put(prefix + "/original_referral/narrative", "Default Narrative");
            }
            
            index++;
        }

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        rawComposition.put("cancer_mdt_output_report/context/start_time", currentDateTime);
        
        for (Map.Entry<String, Object> entry : rawComposition.entrySet()) {
            content.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        return new CancerMDTUpdate(content);
    }

}
