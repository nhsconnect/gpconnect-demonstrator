package uk.gov.hscic.patient.problems.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.problems.model.ProblemListHTML;

/**
 */
public interface ProblemSearch extends Repository {

    List<ProblemListHTML> findAllProblemHTMLTables(String patientId);
}
