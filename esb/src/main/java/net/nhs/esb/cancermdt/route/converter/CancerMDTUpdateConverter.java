package net.nhs.esb.cancermdt.route.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.nhs.esb.cancermdt.model.CancerMDT;
import net.nhs.esb.cancermdt.model.CancerMDTComposition;
import net.nhs.esb.cancermdt.model.CancerMDTUpdate;
import net.nhs.esb.cancermdt.model.CancerMDTparticipation;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class CancerMDTUpdateConverter {

    @Converter
    public CancerMDTUpdate convertCompositionToCancerMDTUpdate(CancerMDT composition) {

        CancerMDT cancerMDT = composition;
        Map<String, String> rawComposition = null;

        int index = 0;

        SimpleDateFormat openEHRDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentDateTime = openEHRDateFormat.format(Calendar.getInstance().getTime());

        rawComposition = cancerMDT.getRawComposition();


        String prefix = "cancer_mdt_output_report/referral_details:" + index;

        // Add the updated or created fields passed from web page
        rawComposition.put(prefix + "/original_referral/request:0/service_requested", cancerMDT.getService());
        rawComposition.put("cancer_mdt_output_report/plan_and_requested_actions:" + index + "/recommendation:0/recommendation", cancerMDT.getNotes());
        

        // Set defailt values for fields requred in openEHR validation
        if (rawComposition.get(prefix + "/mdt_referral/request:0/service_requested") == null) {
            rawComposition.put(prefix + "/mdt_referral/request:0/service_requested", "MDT referral");
        }

        if (rawComposition.get(prefix + "/mdt_referral/request:0/date_or_time_service_required") == null) {
            rawComposition.put(prefix + "/mdt_referral/request:0/date_or_time_service_required", currentDateTime);
        }
        if (rawComposition.get(prefix + "/mdt_referral/request:0/timing") == null) {
            rawComposition.put(prefix + "/mdt_referral/request:0/timing", currentDateTime);
        }
        if (rawComposition.get(prefix + "/original_referral/request:0/timing") == null) {
            rawComposition.put(prefix + "/original_referral/request:0/timing", currentDateTime);
        }
        if (rawComposition.get(prefix + "/mdt_referral/narrative") == null) {
            rawComposition.put(prefix + "/mdt_referral/narrative", "Default Narrative");
        }
        if (rawComposition.get(prefix + "/original_referral/narrative") == null) {
            rawComposition.put(prefix + "/original_referral/narrative", "Default Narrative");
        }

        Map<String, String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        //rawComposition.put("cancer_mdt_output_report/context/start_time", currentDateTime);
        for (Map.Entry<String, String> entry : rawComposition.entrySet()){
            
            if(entry.getKey().startsWith("ctx/participation_name:") ||
                    entry.getKey().startsWith("ctx/participation_function:") ||
                    entry.getKey().startsWith("ctx/participation_mode:") ||
                    entry.getKey().startsWith("ctx/participation_id:")){
                // This list is excluded so do nothing
            } else {
                // Copy all existing and updated fields
                content.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        
        List<CancerMDTparticipation> participationList = cancerMDT.getParticipation();
        
        /*
        if(participationList != null){
            for(int participantIndex = 0; participantIndex < participationList.size(); participantIndex++){
                CancerMDTparticipation participant = participationList.get(participantIndex);
                content.put("ctx/participation_name:" + participantIndex, participant.getName());
                content.put("ctx/participation_function:" + participantIndex, participant.getFunction());
                content.put("ctx/participation_mode:" + participantIndex, participant.getMode());
                content.put("ctx/participation_id:" + participantIndex, String.valueOf(participant.getId()));
            }
        }
        */
        
        /*
        content.put("ctx/participation_name:0", "Dr. Marcus Johnson");
        content.put("ctx/participation_function:0", "Oncologist");
        content.put("ctx/participation_mode:0", "face-to-face communication");
        content.put("ctx/participation_id:0", "1345678");

        content.put("ctx/participation_name:1", "Heather Smith");
        content.put("ctx/participation_function:1", "McMillan Nurse");
        content.put("ctx/participation_mode:1", "face-to-face communication");
        content.put("ctx/participation_id:1", "365672345");
        */
        return new CancerMDTUpdate(content);
    }

}
