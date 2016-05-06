package uk.gov.hscic.patient.procedures.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.procedures.model.ProcedureListHTML;

import java.util.List;

public interface ProcedureSearch extends Repository {

    List<ProcedureListHTML> findAllProceduresHTMLTables(String patientId);

}
