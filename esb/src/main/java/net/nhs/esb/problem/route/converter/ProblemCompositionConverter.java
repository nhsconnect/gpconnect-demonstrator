package net.nhs.esb.problem.route.converter;

import java.util.List;
import java.util.Map;

import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import net.nhs.esb.problem.model.Problem;
import net.nhs.esb.problem.model.ProblemComposition;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ProblemCompositionConverter extends BaseCompositionConverter<Problem> {

    private static final String PROBLEM_UID = "problem_list/_uid";
    private static final String PROBLEM_DEFINITION = "problem_list/problems_and_issues:0/problem_diagnosis:";

    @Converter
    public ProblemComposition convertResponseToProblemComposition(CompositionResponseData response) {

        Map<String, Object> rawComposition = response.getComposition();

        String compositionId = MapUtils.getString(rawComposition, PROBLEM_UID);
        List<Problem> problemList = extractCompositionData(rawComposition);

        ProblemComposition problemComposition = new ProblemComposition();
        problemComposition.setCompositionId(compositionId);
        problemComposition.setProblems(problemList);

        return problemComposition;
    }

    @Override
    protected Problem create(Map<String,Object> rawComposition, String prefix) {

        String problemText = getString(rawComposition, prefix, "/problem_diagnosis|value", "/problem_diagnosis");
        String description = getString(rawComposition, prefix, "/description");
        String code = getString(rawComposition, prefix, "/problem_diagnosis|code");
        String terminology = getString(rawComposition, prefix, "/problem_diagnosis|terminology");
        String dateOfOnset = getString(rawComposition, prefix, "/date_of_onset");

        Problem problem = new Problem();
        problem.setProblem(problemText);
        problem.setDescription(description);
        problem.setCode(code);
        problem.setTerminology(terminology);
        problem.setDateOfOnset(dateOfOnset);
        problem.setSource("openehr");

        return problem;
    }

    private String getString(Map<String,Object> rawComposition, String prefix, String...locations) {

        for (String location : locations) {

            String value = MapUtils.getString(rawComposition, prefix + location);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }

        return null;
    }

    @Override
    protected String dataDefinitionPrefix() {
        return PROBLEM_DEFINITION;
    }
}
