package org.rippleosi.patient.procedures.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.rippleosi.patient.procedures.model.ProcedureSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRProcedureSearch extends AbstractOpenEhrService implements ProcedureSearch {

    @Override
    public List<ProcedureSummary> findAllProcedures(String patientId) {
        ProcedureSummaryQueryStrategy query = new ProcedureSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public ProcedureDetails findProcedure(String patientId, String procedureId) {
        ProcedureDetailsQueryStrategy query = new ProcedureDetailsQueryStrategy(patientId, procedureId);

        return findData(query);
    }
}
