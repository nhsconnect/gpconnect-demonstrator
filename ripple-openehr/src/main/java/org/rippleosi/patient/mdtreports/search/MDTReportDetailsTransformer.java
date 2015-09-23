package org.rippleosi.patient.mdtreports.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.mdtreports.model.MDTReportDetails;

/**
 */
public class MDTReportDetailsTransformer implements Transformer<Map<String, Object>, MDTReportDetails> {

    @Override
    public MDTReportDetails transform(Map<String, Object> input) {

        Date dateOfRequest = DateFormatter.toDate(MapUtils.getString(input, "date_created"));
        Date dateOfMeeting = DateFormatter.toDateOnly(MapUtils.getString(input, "meeting_date"));
        Date timeOfMeeting = DateFormatter.toTimeOnly(MapUtils.getString(input, "meeting_date"));

        MDTReportDetails mdtReport = new MDTReportDetails();
        mdtReport.setSource("openehr");
        mdtReport.setSourceId(MapUtils.getString(input, "uid"));
        mdtReport.setServiceTeam(MapUtils.getString(input, "service_team"));
        mdtReport.setDateOfRequest(dateOfRequest);
        mdtReport.setDateOfMeeting(dateOfMeeting);
        mdtReport.setTimeOfMeeting(timeOfMeeting);
        mdtReport.setQuestion(MapUtils.getString(input, "question"));
        mdtReport.setNotes(MapUtils.getString(input, "notes"));

        return mdtReport;
    }
}
