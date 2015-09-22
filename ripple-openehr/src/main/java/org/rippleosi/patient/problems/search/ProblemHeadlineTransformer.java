package org.rippleosi.patient.problems.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.problems.model.ProblemHeadline;

/**
 */
public class ProblemHeadlineTransformer implements Transformer<Map<String, Object>, ProblemHeadline> {

    @Override
    public ProblemHeadline transform(Map<String, Object> input) {

        ProblemHeadline problem = new ProblemHeadline();
        problem.setSource("openehr");
        problem.setSourceId(MapUtils.getString(input, "uid"));
        problem.setProblem(MapUtils.getString(input, "problem"));

        return problem;
    }
}
