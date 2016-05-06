package uk.gov.hscic.patient.procedures.search;

import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.procedures.model.ProcedureListHTML;

import java.util.List;

public class NotConfiguredProcedureSearch implements ProcedureSearch {

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.NONE;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ProcedureListHTML> findAllProceduresHTMLTables(final String patientId) {
        throw ConfigurationException.unimplementedTransaction(ProcedureSearch.class);
    }

}
