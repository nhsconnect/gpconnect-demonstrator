package net.nhs.esb.problem.route.converter;

import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.problem.model.Problem;
import net.nhs.esb.problem.model.ProblemComposition;
import net.nhs.esb.problem.model.ProblemUpdate;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ProblemUpdateConverter {

    @Converter
    public ProblemUpdate convertCompositionToProblemUpdate(ProblemComposition composition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        int index = 0;
        for (Problem problem : composition.getProblems()) {

            String prefix = "problem_list/problems_and_issues:0/problem_diagnosis:" + index;

            content.put(prefix + "/problem_diagnosis|value", problem.getProblem());
            content.put(prefix + "/description", problem.getDescription());
            content.put(prefix + "/problem_diagnosis|code", problem.getCode());
            content.put(prefix + "/problem_diagnosis|terminology", problem.getTerminology());
            content.put(prefix + "/date_of_onset", problem.getDateOfOnset());

            index++;
        }

        return new ProblemUpdate(content);
    }
}
