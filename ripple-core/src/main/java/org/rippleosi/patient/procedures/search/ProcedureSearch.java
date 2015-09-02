package org.rippleosi.patient.procedures.search;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.rippleosi.patient.procedures.model.ProcedureSummaries;

/**
 */
public interface ProcedureSearch extends Repository {

    ProcedureSummaries findAllProcedures(String patientId);

    ProcedureDetails findProcedure(String patientId, String procedureId);

}
