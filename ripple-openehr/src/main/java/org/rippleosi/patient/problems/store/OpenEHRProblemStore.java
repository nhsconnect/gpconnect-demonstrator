package org.rippleosi.patient.problems.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.common.service.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRProblemStore extends AbstractOpenEhrService implements ProblemStore {

    @Value("${openehr.problemTemplate}")
    private String problemTemplate;

    private static final String PROBLEM_PREFIX = "problem_list/problems_and_issues:0/problem_diagnosis:0";

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.Problems.Create")
    public void create(String patientId, ProblemDetails problem) {

        Map<String, Object> content = createFlatJsonContent(problem);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, problemTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.OpenEHR.VirtualTopic.Ripple.Problems.Update")
    public void update(String patientId, ProblemDetails problem) {

        Map<String, Object> content = createFlatJsonContent(problem);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(problem.getSourceId(), patientId, problemTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String, Object> createFlatJsonContent(ProblemDetails problem) {

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        if (problem.getCode() == null || problem.getCode().isEmpty()) {
            problem.setCode("00001");
        }

        if (problem.getTerminology() == null || problem.getTerminology().isEmpty()) {
            problem.setTerminology("Std");
        }

        String dateOfOnset = DateFormatter.toString(problem.getDateOfOnset());

        content.put(PROBLEM_PREFIX + "/problem_diagnosis|value", problem.getProblem());
        content.put(PROBLEM_PREFIX + "/description", problem.getDescription());
        content.put(PROBLEM_PREFIX + "/problem_diagnosis|code", problem.getCode());
        content.put(PROBLEM_PREFIX + "/problem_diagnosis|terminology", problem.getTerminology());
        content.put(PROBLEM_PREFIX + "/date_of_onset", dateOfOnset);

        return content;
    }
}
