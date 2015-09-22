package org.rippleosi.patient.labresults.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.labresults.model.LabResultSummary;

/**
 */
public class LabResultSummaryTransformer implements Transformer<Map<String, Object>, LabResultSummary> {

    @Override
    public LabResultSummary transform(Map<String, Object> input) {

        Date sampleTaken = DateFormatter.toDate(MapUtils.getString(input, "sample_taken"));
        Date dateCreated = DateFormatter.toDate(MapUtils.getString(input, "date_created"));

        LabResultSummary labResult = new LabResultSummary();
        labResult.setSource("openehr");
        labResult.setSourceId(MapUtils.getString(input, "uid"));
        labResult.setTestName(MapUtils.getString(input, "test_name"));
        labResult.setSampleTaken(sampleTaken);
        labResult.setDateCreated(dateCreated);

        return labResult;
    }
}
