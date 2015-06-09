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

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        List<CancerMDT> cancerMDTs = composition.getCancerMDT();

        int index = 0;

        for (CancerMDT cancerMDT : cancerMDTs) {

            content.put("cancer_mdt_output_report/referral_details:" + index + "/original_referral/request:0/service_requested", cancerMDT.getService());
            content.put("cancer_mdt_output_report/plan_and_requested_actions:" + index + "/recommendation:0/recommendation", cancerMDT.getNotes());
            
            //content.put("cancer_mdt_output_report/referral_details:" + index + "/mdt_referral/request:0/date_or_time_service_required", cancerMDT.getDate());

            index++;
        }

        return new CancerMDTUpdate(content);
    }

}
