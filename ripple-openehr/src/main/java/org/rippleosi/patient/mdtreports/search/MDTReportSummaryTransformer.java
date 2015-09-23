package org.rippleosi.patient.mdtreports.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;

/**
 */
public class MDTReportSummaryTransformer implements Transformer<Map<String, Object>, MDTReportSummary> {

    @Override
    public MDTReportSummary transform(Map<String, Object> input) {

        Date dateOfRequest = DateFormatter.toDate(MapUtils.getString(input, "date_created"));
        Date dateOfMeeting = DateFormatter.toDateOnly(MapUtils.getString(input, "meeting_date"));

        MDTReportSummary mdtReport = new MDTReportSummary();
        mdtReport.setSource("openehr");
        mdtReport.setSourceId(MapUtils.getString(input, "uid"));
        mdtReport.setServiceTeam(MapUtils.getString(input, "service_team"));
        mdtReport.setDateOfRequest(dateOfRequest);
        mdtReport.setDateOfMeeting(dateOfMeeting);

        return mdtReport;
    }
}
