package org.rippleosi.patient.problems.search;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemDetails;

/**
 */
public class ProblemDetailsTransformer implements Transformer<Map<String, Object>, ProblemDetails> {

    @Override
    public ProblemDetails transform(Map<String, Object> input) {
        Date dateOfOnset = DateFormatter.toDate(MapUtils.getString(input, "onset_date"));

        ProblemDetails problem = new ProblemDetails();
        problem.setSource("openehr");
        problem.setSourceId(MapUtils.getString(input, "uid"));
        problem.setProblem(MapUtils.getString(input, "problem"));
        problem.setDateOfOnset(dateOfOnset);
        problem.setCode(MapUtils.getString(input, "problem_code"));
        problem.setTerminology(MapUtils.getString(input, "problem_terminology"));
        problem.setDescription(MapUtils.getString(input, "description"));

        return problem;
    }
}
