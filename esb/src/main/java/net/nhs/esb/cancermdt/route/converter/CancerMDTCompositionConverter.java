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
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

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
        
        
        // Get the participants for the MDT
        
        //int countParticipants = countNumberOfElements(rawComposition, "ctx/participation_name:");
        int countParticipants = 0;
        
        if(countParticipants > 0){
            List<CancerMDTparticipation> participationList = new ArrayList<>();

            for(int participantIndex = 0; participantIndex < countParticipants; participantIndex++){

                CancerMDTparticipation participant = new CancerMDTparticipation();
                participant.setName(MapUtils.getString(rawComposition, "ctx/participation_name:" + participantIndex));
                participant.setFunction(MapUtils.getString(rawComposition, "ctx/participation_function:" + participantIndex));
                participant.setMode(MapUtils.getString(rawComposition, "ctx/participation_mode:" + participantIndex));
                participant.setId(Long.valueOf(MapUtils.getString(rawComposition, "ctx/participation_id:" + participantIndex)));

                participationList.add(participant);
            }

            cancerMDT.setParticipation(participationList);
        }
        
        List<CancerMDTparticipation> participationList = new ArrayList<>();

        CancerMDTparticipation participant = new CancerMDTparticipation();
        participant.setName("Dr. Marcus Johnson");
        participant.setFunction("Oncologist");
        participant.setMode("face-to-face communication");
        participant.setId(Long.valueOf("1345678"));
        participationList.add(participant);
        
        CancerMDTparticipation participant2 = new CancerMDTparticipation();
        participant2.setName("Heather Smith");
        participant2.setFunction("McMillan Nurse");
        participant2.setMode("face-to-face communication");
        participant2.setId(Long.valueOf("365672345"));
        participationList.add(participant2);
        
        cancerMDT.setParticipation(participationList);
        
        
        CancerMDTComposition cancerMDTComposition = new CancerMDTComposition();
        cancerMDTComposition.setCancerMDT(cancerMDT);

        return cancerMDTComposition;
    }

    
    public int countNumberOfElements(Map<String, Object> rawComposition, String elementString){
        
        int maxEntry = -1;
        
        for (String key : rawComposition.keySet()) {
            if (StringUtils.startsWith(key, elementString)) {
                String index = StringUtils.substringAfter(key, elementString);
                index = StringUtils.substringBefore(index, "/");

                maxEntry = Math.max(maxEntry, Integer.parseInt(index));
            }
        }

        return maxEntry + 1;
    }
    
}
