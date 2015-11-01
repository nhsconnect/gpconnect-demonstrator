package org.rippleosi.patient.problems.search.search;

import java.util.List;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractVistaRequestStrategy;
import org.rippleosi.patient.problems.model.ProblemDetails;
import org.rippleosi.patient.problems.search.model.VistaProblem;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class VistaProblemToDetailRequestStrategy extends AbstractVistaRequestStrategy<VistaProblem, ProblemDetails> {

    private final String problemId;
    private final String vistaAddress;

    protected VistaProblemToDetailRequestStrategy(String patientId, String problemId, String vistaAddress) {
        super(patientId);
        this.problemId = problemId;
        this.vistaAddress = vistaAddress;
    }

    @Override
    public UriComponents getQueryUriComponents() {
        return UriComponentsBuilder
            .fromHttpUrl(vistaAddress + "vistADomain")
            .queryParam("domain", "PROBLEM")
            .queryParam("patient", getPatientId())
            .build();
    }

    @Override
    public ProblemDetails transform(List<VistaProblem> resultSet) {
        for (VistaProblem problem : resultSet) {

            if (problem.getUid().equals(problemId)) {
                return new VistaProblemToDetailTransformer().transform(problem);
            }
        }

        throw new DataNotFoundException("Vista did not return a result for problem: " + problemId);
    }
}
