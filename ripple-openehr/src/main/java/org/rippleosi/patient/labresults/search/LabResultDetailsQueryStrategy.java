package org.rippleosi.patient.labresults.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractQueryStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class LabResultDetailsQueryStrategy extends AbstractQueryStrategy<LabResultDetails> {

    private static final Logger logger = LoggerFactory.getLogger(LabResultDetailsQueryStrategy.class);

    private final String labResultId;

    protected LabResultDetailsQueryStrategy(String patientId, String labResultId) {
        super(patientId);
        this.labResultId = labResultId;
    }

    @Override
    public String getQuery(String ehrId) {
        return "select a/uid/value as uid, " +
                "a/composer/name as author, " +
                "a/context/start_time/value as date_created, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0005]/value/value as test_name, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0057]/value/value as conclusion, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0073]/value/value as status, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[at0075]/value/value as sample_taken, " +
                "a_a/data[at0001]/events[at0002]/data[at0003]/items[openEHR-EHR-CLUSTER.laboratory_test_panel.v0] as test_panel " +
                "from EHR e[ehr_id/value='" + ehrId + "'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report-result.v1] " +
                "contains OBSERVATION a_a[openEHR-EHR-OBSERVATION.laboratory_test.v0] " +
                "where a/name/value='Laboratory test report' " +
                "and a/uid/value='" + labResultId + "' ";
    }

    @Override
    public LabResultDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        Date sampleTaken = DateFormatter.toDate(MapUtils.getString(data, "sample_taken"));
        Date dateCreated = DateFormatter.toDate(MapUtils.getString(data, "date_created"));

        LabResultDetails labResult = new LabResultDetails();
        labResult.setSource("openehr");
        labResult.setSourceId(MapUtils.getString(data, "uid"));
        labResult.setTestName(MapUtils.getString(data, "test_name"));
        labResult.setSampleTaken(sampleTaken);
        labResult.setDateCreated(dateCreated);
        labResult.setAuthor(MapUtils.getString(data, "author"));
        labResult.setConclusion(MapUtils.getString(data, "conclusion"));
        labResult.setStatus(MapUtils.getString(data, "status"));

        List<LabResultDetails.TestResult> testResults = createTestResults(data);

        labResult.setTestResults(testResults);

        return labResult;
    }

    private List<LabResultDetails.TestResult> createTestResults(Map<String, Object> data) {

        List<Map<String, Object>> labResults = extractLabResults(data);

        return CollectionUtils.collect(labResults, new TestResultTransformer(), new ArrayList<>());
    }

    private List<Map<String, Object>> extractLabResults(Map<String, Object> source) {
        try {
            return (List<Map<String, Object>>)PropertyUtils.getNestedProperty(source, "test_panel.items");
        } catch (Exception ex) {
            logger.debug("{}: {}", ex.getClass().getName(), ex.getMessage());
            return Collections.emptyList();
        }
    }
}
