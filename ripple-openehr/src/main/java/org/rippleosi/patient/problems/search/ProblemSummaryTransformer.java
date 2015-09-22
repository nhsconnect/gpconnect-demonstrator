package org.rippleosi.patient.problems.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemSummary;

/**
 */
public class ProblemSummaryTransformer implements Transformer<Map<String, Object>, ProblemSummary> {

    @Override
    public ProblemSummary transform(Map<String, Object> input) {

        Date dateOfOnset = DateFormatter.toDate(MapUtils.getString(input, "onset_date"));

        ProblemSummary problem = new ProblemSummary();
        problem.setSource("openehr");
        problem.setSourceId(MapUtils.getString(input, "uid"));
        problem.setProblem(MapUtils.getString(input, "problem"));
        problem.setDateOfOnset(dateOfOnset);

        return problem;
    }
}
