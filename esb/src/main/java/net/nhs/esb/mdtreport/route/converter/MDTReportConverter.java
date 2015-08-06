package net.nhs.esb.mdtreport.route.converter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.mdtreport.model.MDTReportComposition;
import net.nhs.esb.mdtreport.model.MDTReportUpdate;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.util.DateFormatter;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class MDTReportConverter {

    @Converter
    public MDTReportComposition convertResponseToMDTReportComposition(CompositionResponseData responseData) {

        Map<String, Object> rawComposition = responseData.getComposition();

        String compositionId = MapUtils.getString(rawComposition, "mdt_output_report/_uid");
        String service = MapUtils.getString(rawComposition, "mdt_output_report/referral_details/mdt_referral/mdt_team");
        String questionForMDT = MapUtils.getString(rawComposition, "mdt_output_report/history/question_to_mdt/question_to_mdt");
        String meetingDiscussion = MapUtils.getString(rawComposition, "mdt_output_report/plan_and_requested_actions/recommendation/meeting_notes");

        String requestDateTime = MapUtils.getString(rawComposition, "mdt_output_report/context/start_time");
        String meetingDateTime = MapUtils.getString(rawComposition, "mdt_output_report/referral_details/referral_tracking/time");

        Date dateOfRequest = DateFormatter.toDate(requestDateTime);
        Date dateOfMeeting = DateFormatter.toDateOnly(meetingDateTime);
        Date timeOfMeeting = DateFormatter.toTimeOnly(meetingDateTime);

        MDTReportComposition mdtReportComposition = new MDTReportComposition();
        mdtReportComposition.setCompositionId(compositionId);
        mdtReportComposition.setService(service);
        mdtReportComposition.setDateOfRequest(dateOfRequest);
        mdtReportComposition.setDateOfMeeting(dateOfMeeting);
        mdtReportComposition.setTimeOfMeeting(timeOfMeeting);
        mdtReportComposition.setQuestionForMDT(questionForMDT);
        mdtReportComposition.setMeetingDiscussion(meetingDiscussion);
        mdtReportComposition.setSource("openehr");

        return mdtReportComposition;
    }

    @Converter
    public MDTReportUpdate convertMDTReportCompositionToUpdate(MDTReportComposition mdtReportComposition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");
        content.put("ctx/id_scheme", "NHS");
        content.put("ctx/id_namespace", "NHS");

        String meetingTime = DateFormatter.combineDateTime(mdtReportComposition.getDateOfMeeting(), mdtReportComposition.getTimeOfMeeting());

        content.put("mdt_output_report/referral_details/mdt_referral/mdt_team", mdtReportComposition.getService());
        content.put("mdt_output_report/history/question_to_mdt/question_to_mdt", mdtReportComposition.getQuestionForMDT());
        content.put("mdt_output_report/plan_and_requested_actions/recommendation/meeting_notes", mdtReportComposition.getMeetingDiscussion());
        content.put("mdt_output_report/context/start_time", DateFormatter.toString(mdtReportComposition.getDateOfRequest()));

        content.put("mdt_output_report/referral_details/referral_tracking/time", meetingTime);
        content.put("mdt_output_report/referral_details/mdt_referral/narrative", "MDT Referral");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/current_state|code", "526");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/current_state|value", "planned");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/careflow_step|code", "at0002");
        content.put("mdt_output_report/referral_details/referral_tracking/ism_transition/careflow_step|value", "Referral planned");

        return new MDTReportUpdate(content);
    }
}
