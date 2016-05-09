package uk.gov.hscic.patient.immunisations.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.immunisations.model.ImmunisationListHTML;

public interface ImmunisationSearch extends Repository {

    List<ImmunisationListHTML> findAllImmunisationHTMLTables(String patientId);
}
