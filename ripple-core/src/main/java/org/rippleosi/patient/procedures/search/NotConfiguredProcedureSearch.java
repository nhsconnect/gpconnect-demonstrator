package org.rippleosi.patient.procedures.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.procedures.model.ProcedureDetails;
import org.rippleosi.patient.procedures.model.ProcedureSummary;

/**
 */
public class NotConfiguredProcedureSearch implements ProcedureSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ProcedureSummary> findAllProcedures(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ProcedureSearch.class);
    }

    @Override
    public ProcedureDetails findProcedure(String patientId, String procedureId) {
        throw ConfigurationException.unimplementedTransaction(ProcedureSearch.class);
    }

}
