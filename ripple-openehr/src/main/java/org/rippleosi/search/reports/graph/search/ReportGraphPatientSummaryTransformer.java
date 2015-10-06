package org.rippleosi.search.reports.graph.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.search.reports.graph.model.ReportGraphPatientSummary;

public class ReportGraphPatientSummaryTransformer implements Transformer<Map<String, Object>, ReportGraphPatientSummary> {

    @Override
    public ReportGraphPatientSummary transform(Map<String, Object> input) {
        ReportGraphPatientSummary summary = new ReportGraphPatientSummary();

        summary.setSource("openehr");
        summary.setSourceId(MapUtils.getString(input, "uid"));
        summary.setNhsNumber(MapUtils.getString(input, "nhsNumber"));

        String dob = MapUtils.getString(input, "dob");
        summary.setDateOfBirth(DateFormatter.toDate(dob));

        return summary;
    }
}
