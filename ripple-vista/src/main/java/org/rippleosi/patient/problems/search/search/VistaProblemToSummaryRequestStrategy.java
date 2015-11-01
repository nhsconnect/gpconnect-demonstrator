package org.rippleosi.patient.problems.search.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.AbstractVistaRequestStrategy;
import org.rippleosi.patient.problems.model.ProblemSummary;
import org.rippleosi.patient.problems.search.model.VistaProblem;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class VistaProblemToSummaryRequestStrategy
    extends AbstractVistaRequestStrategy<VistaProblem, List<ProblemSummary>> {

    private final String vistaAddress;

    protected VistaProblemToSummaryRequestStrategy(String patientId, String vistaAddress) {
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
    public List<ProblemSummary> transform(List<VistaProblem> resultSet) {
        return CollectionUtils.collect(resultSet, new VistaProblemToSummaryTransformer(), new ArrayList<>());
    }
}
