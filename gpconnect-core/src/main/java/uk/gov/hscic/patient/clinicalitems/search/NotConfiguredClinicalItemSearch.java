package uk.gov.hscic.patient.clinicalitems.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;

import java.util.List;

public class NotConfiguredClinicalItemSearch implements ClinicalItemSearch {

    @Override
    public RepoSource getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ClinicalItemListHTML> findAllClinicalItemHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(ClinicalItemSearch.class);
    }
}
