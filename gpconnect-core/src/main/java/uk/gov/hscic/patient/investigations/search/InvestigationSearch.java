package uk.gov.hscic.patient.investigations.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.investigations.model.InvestigationListHTML;

import java.util.List;

public interface InvestigationSearch extends Repository {

    List<InvestigationListHTML> findAllInvestigationHTMLTables(String patientId);
}
