package uk.gov.hscic.patient.observations.search;

import java.util.List;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.observations.model.ObservationListHTML;

/**
 */
public class NotConfiguredObservationSearch implements ObservationSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ObservationListHTML> findAllObservationHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(ObservationSearch.class);
    }
}
