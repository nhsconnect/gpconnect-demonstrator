package uk.gov.hscic.patient.encounters.search;

import java.util.Date;
import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;

public interface EncounterSearch extends Repository {

    List<EncounterListHTML> findAllEncounterHTMLTables(String patientId, Date fromDate, Date toDate);
}
