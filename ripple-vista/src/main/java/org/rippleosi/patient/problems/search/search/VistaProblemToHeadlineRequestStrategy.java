package org.rippleosi.patient.problems.search.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractVistaRequestStrategy;
import org.rippleosi.patient.problems.model.ProblemHeadline;
import org.rippleosi.patient.problems.search.model.VistaProblem;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class VistaProblemToHeadlineRequestStrategy
    extends AbstractVistaRequestStrategy<VistaProblem, List<ProblemHeadline>> {

    private final String vistaAddress;

    protected VistaProblemToHeadlineRequestStrategy(String patientId, String vistaAddress) {
        super(patientId);
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
    public List<ProblemHeadline> transform(List<VistaProblem> resultSet) {
        return CollectionUtils.collect(resultSet, new VistaProblemToHeadlineTransformer(), new ArrayList<>());
    }
}
