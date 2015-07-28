package net.nhs.esb.mdtreport.route.converter;

import java.util.Map;

import net.nhs.esb.mdtreport.model.MDTReportComposition;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
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

        String dateOfRequest = StringUtils.substringBefore(requestDateTime, "T");
        String dateOfMeeting = StringUtils.substringBefore(meetingDateTime, "T");
        String timeOfMeeting = StringUtils.substringBeforeLast(StringUtils.substringAfter(meetingDateTime, "T"), ":");

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
}
