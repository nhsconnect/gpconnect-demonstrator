package uk.gov.hscic.patient.observations.search;

import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.service.AbstractOpenEhrService;
import uk.gov.hscic.patient.observations.model.ObservationDetails;
import uk.gov.hscic.patient.observations.model.ObservationListHTML;
import uk.gov.hscic.patient.observations.model.ObservationSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRObservationSearch extends AbstractOpenEhrService implements ObservationSearch {

    public List<ObservationSummary> findAllLabResults(String patientId) {
        LabResultSummaryQueryStrategy query = new LabResultSummaryQueryStrategy(patientId);

        return findData(query);
    }

    public ObservationDetails findLabResult(String patientId, String labResultId) {
        LabResultDetailsQueryStrategy query = new LabResultDetailsQueryStrategy(patientId, labResultId);

        return findData(query);
    }

    @Override
    public List<ObservationListHTML> findAllObservationHTMLTables(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ObservationSearch.class);
    }
}
