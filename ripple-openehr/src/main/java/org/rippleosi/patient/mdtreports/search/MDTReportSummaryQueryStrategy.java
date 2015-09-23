package org.rippleosi.patient.mdtreports.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.mdtreports.model.MDTReportSummary;

/**
 */
public class MDTReportSummaryQueryStrategy extends AbstractListQueryStrategy<MDTReportSummary> {

    MDTReportSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/context/start_time/value as date_created, " +
                "b_a/items/protocol/items[at0011]/value/value as service_team, " +
                "b_a/items/activities[at0001]/timing/value as meeting_date " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report.v1] " +
                "contains ( " +
                "    SECTION b_a[openEHR-EHR-SECTION.referral_details_rcp.v1] and " +
                "    SECTION b_b[openEHR-EHR-SECTION.history_rcp.v1] and " +
                "    SECTION b_c[openEHR-EHR-SECTION.plan_requested_actions_rcp.v1]) " +
                "where a/name/value='MDT Output Report'";
    }

    @Override
    public List<MDTReportSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new MDTReportSummaryTransformer(), new ArrayList<>());
    }
}
