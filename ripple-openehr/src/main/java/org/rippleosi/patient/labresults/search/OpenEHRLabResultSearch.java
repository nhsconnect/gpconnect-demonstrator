package org.rippleosi.patient.labresults.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.rippleosi.patient.labresults.model.LabResultSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRLabResultSearch extends AbstractOpenEhrService implements LabResultSearch {

    @Override
    public List<LabResultSummary> findAllLabResults(String patientId) {
        LabResultSummaryQueryStrategy query = new LabResultSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public LabResultDetails findLabResult(String patientId, String labResultId) {
        LabResultDetailsQueryStrategy query = new LabResultDetailsQueryStrategy(patientId, labResultId);

        return findData(query);
    }
}
