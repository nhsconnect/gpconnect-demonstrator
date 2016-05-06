package uk.gov.hscic.patient.procedures.search;

import java.util.List;

import uk.gov.hscic.common.service.AbstractOpenEhrService;
import uk.gov.hscic.patient.procedures.model.ProcedureDetails;
import uk.gov.hscic.patient.procedures.model.ProcedureListHTML;
import uk.gov.hscic.patient.procedures.model.ProcedureSummary;
import org.springframework.stereotype.Service;

import static uk.gov.hscic.common.exception.ConfigurationException.*;

/**
 */
@Service
public class OpenEHRProcedureSearch extends AbstractOpenEhrService implements ProcedureSearch {

    public List<ProcedureSummary> findAllProcedures(String patientId) {
        ProcedureSummaryQueryStrategy query = new ProcedureSummaryQueryStrategy(patientId);

        return findData(query);
    }

    public ProcedureDetails findProcedure(String patientId, String procedureId) {
        ProcedureDetailsQueryStrategy query = new ProcedureDetailsQueryStrategy(patientId, procedureId);

        return findData(query);
    }

    @Override
    public List<ProcedureListHTML> findAllProceduresHTMLTables(String patientId) {
        throw unimplementedTransaction(ProcedureSearch.class);
    }
}
