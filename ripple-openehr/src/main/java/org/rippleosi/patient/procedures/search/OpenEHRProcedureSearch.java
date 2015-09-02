package org.rippleosi.patient.procedures.search;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.rippleosi.patient.procedures.model.ProcedureSummaries;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRProcedureSearch extends AbstractOpenEhrService implements ProcedureSearch {

    @Override
    public ProcedureSummaries findAllProcedures(String patientId) {
        ProcedureSummaryQueryStrategy query = new ProcedureSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public ProcedureDetails findProcedure(String patientId, String procedureId) {
        ProcedureDetailsQueryStrategy query = new ProcedureDetailsQueryStrategy(patientId, procedureId);

        return findData(query);
    }
}
