package org.rippleosi.patient.procedures.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.rippleosi.patient.procedures.model.ProcedureSummary;

/**
 */
public interface ProcedureSearch extends Repository {

    List<ProcedureSummary> findAllProcedures(String patientId);

    ProcedureDetails findProcedure(String patientId, String procedureId);

}
