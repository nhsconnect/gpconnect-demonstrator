package uk.gov.hscic.patient.investigations.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.investigations.model.InvestigationListHtml;

public interface InvestigationSearch extends Repository {
    InvestigationListHtml findInvestigationListHtml(String patientId);
}
