package org.rippleosi.patient.labresults.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.patient.labresults.model.LabResultSummary;

/**
 */
public class LabResultSummaryQueryStrategy extends AbstractListQueryStrategy<LabResultSummary> {

    LabResultSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/context/start_time/value as date_created, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0005]/value/value as test_name, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0075]/value/value as sample_taken " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report-result.v1] " +
                "contains OBSERVATION a_a[openEHR-EHR-OBSERVATION.laboratory_test.v0] " +
                "where a/name/value='Laboratory test report' ";
    }

    @Override
    public List<LabResultSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new LabResultSummaryTransformer(), new ArrayList<>());
    }
}
