package net.nhs.esb.cancermdt.route.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.nhs.esb.cancermdt.model.CancerMDT;
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
        
        if(rawComposition == null){
            rawComposition = new HashMap<String, String>();
        }

        String prefix = "cancer_mdt_output_report/referral_details:" + index;

        // Add the updated or created fields passed from web page
        rawComposition.put(prefix + "/original_referral/request:0/service_requested", cancerMDT.getService());
        rawComposition.put("cancer_mdt_output_report/plan_and_requested_actions:" + index + "/recommendation:0/recommendation", cancerMDT.getNotes());
        rawComposition.put("cancer_mdt_output_report/history:0/question_for_mdt/question_to_mdt", cancerMDT.getQuestionForMDT());

        try{
            SimpleDateFormat jsonDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Calendar newDateTime = Calendar.getInstance();
            newDateTime.setTime(jsonDateFormat.parse(cancerMDT.getDate()));
            rawComposition.put(prefix + "/mdt_referral/request:0/date_or_time_service_required", openEHRDateFormat.format(newDateTime.getTime()));
        }catch(Exception e){
            // IF the date fails to parse it is because it is the new date format from the date picker which is already in the correct format
            rawComposition.put(prefix + "/mdt_referral/request:0/date_or_time_service_required", cancerMDT.getDate());
        }
        
        // Set defailt values for fields requred in openEHR validation
        if (rawComposition.get(prefix + "/mdt_referral/request:0/service_requested") == null) {
            rawComposition.put(prefix + "/mdt_referral/request:0/service_requested", "MDT referral");
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
        content.put("ctx/id_scheme", "NHS");
        content.put("ctx/id_namespace", "NHS");
        
        List<CancerMDTparticipation> participationList = cancerMDT.getParticipation();
        
        if(participationList != null){
            for(int participantIndex = 0; participantIndex < participationList.size(); participantIndex++){
                CancerMDTparticipation participant = participationList.get(participantIndex);
                content.put("ctx/participation_name:" + participantIndex, participant.getName());
                content.put("ctx/participation_function:" + participantIndex, participant.getFunction());
                content.put("ctx/participation_mode:" + participantIndex, participant.getMode());
                content.put("ctx/participation_id:" + participantIndex, String.valueOf(participant.getId()));
            }
        }
        
        for (Map.Entry<String, String> entry : rawComposition.entrySet()){
            // We don't want to store the old participation information as we have added new information and we don't know who is removed
            if(!entry.getKey().contains("_other_participation:")){
                // Copy all existing and updated fields except the participation fields
                content.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        
       return new CancerMDTUpdate(content);
    }

}
