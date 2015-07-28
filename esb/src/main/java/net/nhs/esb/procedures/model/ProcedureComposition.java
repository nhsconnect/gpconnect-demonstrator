package net.nhs.esb.procedures.model;

import java.util.List;

/**
 */
public class ProcedureComposition {

    private String compositionId;
    private List<Procedure> procedures;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<Procedure> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<Procedure> procedures) {
        this.procedures = procedures;
    }
}
