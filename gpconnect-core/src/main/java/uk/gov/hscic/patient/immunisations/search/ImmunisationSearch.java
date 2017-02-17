package uk.gov.hscic.patient.immunisations.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.immunisations.model.ImmunisationData;

public interface ImmunisationSearch extends Repository {

    List<ImmunisationData> findAllImmunisationHTMLTables(String patientId);
}
