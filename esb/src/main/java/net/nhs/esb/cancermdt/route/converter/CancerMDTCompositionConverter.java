package net.nhs.esb.cancermdt.route.converter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import net.nhs.esb.cancermdt.model.*;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
@Converter
@Component
public class CancerMDTCompositionConverter {

    @Converter
    public CancerMDTComposition convertResponseToCancerMDTComposition(CompositionResponseData response) {

        
        Map<String, Object> rawComposition = response.getComposition();
        
        Map<String, String> rawCompositionAsStrings = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : rawComposition.entrySet()) {
            rawCompositionAsStrings.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        
        CancerMDT cancerMDT = new CancerMDT();
        
        cancerMDT.setRawComposition(rawCompositionAsStrings);
        
        cancerMDT.setCompositionId(MapUtils.getString(rawComposition, "cancer_mdt_output_report/_uid"));
        cancerMDT.setId(0l);
        cancerMDT.setService(MapUtils.getString(rawComposition, "cancer_mdt_output_report/referral_details:0/original_referral/request:0/service_requested"));

        SimpleDateFormat openEHRDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat jsonDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        try {
            Date dateTime = openEHRDateFormat.parse(MapUtils.getString(rawComposition, "cancer_mdt_output_report/referral_details:0/mdt_referral/request:0/date_or_time_service_required"));
            cancerMDT.setDate(jsonDateFormat.format(dateTime));
        }catch(Exception e){
            cancerMDT.setDate(MapUtils.getString(rawComposition, "cancer_mdt_output_report/referral_details:0/mdt_referral/request:0/date_or_time_service_required"));
        }

        cancerMDT.setNotes(MapUtils.getString(rawComposition, "cancer_mdt_output_report/plan_and_requested_actions:0/recommendation:0/recommendation"));
        
        
        CancerMDTComposition cancerMDTComposition = new CancerMDTComposition();
        cancerMDTComposition.setCancerMDT(cancerMDT);

        return cancerMDTComposition;
    }

}
