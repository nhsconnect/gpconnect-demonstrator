package net.nhs.esb.cancermdt.route.converter;

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

        for (CancerMDT cancerMDT : cancerMDTs) {

            if (rawComposition == null) {
                rawComposition = cancerMDT.getRawComposition();
            }

            rawComposition.put("cancer_mdt_output_report/referral_details:" + index + "/original_referral/request:0/service_requested", cancerMDT.getService());
            rawComposition.put("cancer_mdt_output_report/plan_and_requested_actions:" + index + "/recommendation:0/recommendation", cancerMDT.getNotes());

            //content.put("cancer_mdt_output_report/referral_details:" + index + "/original_referral/request:0/service_requested", cancerMDT.getService());
            index++;
        }

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        for (Map.Entry<String, Object> entry : rawComposition.entrySet()) {
            content.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        return new CancerMDTUpdate(content);
    }

}
