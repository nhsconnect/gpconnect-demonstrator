package uk.gov.hscic.patient.immunisations.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;

import uk.gov.hscic.patient.immunisations.model.ImmunisationData;

import java.util.List;

public class NotConfiguredImmunisationSearch implements ImmunisationSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ImmunisationData> findAllImmunisationHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(ImmunisationSearch.class);
    }
}
