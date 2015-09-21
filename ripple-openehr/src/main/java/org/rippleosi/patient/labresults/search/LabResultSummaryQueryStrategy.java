package org.rippleosi.patient.labresults.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.service.AbstractListQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
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

        List<LabResultSummary> labResultList = new ArrayList<>();

        for (Map<String, Object> data : resultSet) {

            Date sampleTaken = DateFormatter.toDate(MapUtils.getString(data, "sample_taken"));
            Date dateCreated = DateFormatter.toDate(MapUtils.getString(data, "date_created"));

            LabResultSummary labResult = new LabResultSummary();
            labResult.setSource("openehr");
            labResult.setSourceId(MapUtils.getString(data, "uid"));
            labResult.setTestName(MapUtils.getString(data, "test_name"));
            labResult.setSampleTaken(sampleTaken);
            labResult.setDateCreated(dateCreated);

            labResultList.add(labResult);
        }

        return labResultList;
    }
}
