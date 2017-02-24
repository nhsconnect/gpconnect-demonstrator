package uk.gov.hscic.patient.encounters.search;

import java.util.Date;
import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.encounters.model.EncounterData;

public interface EncounterSearch extends Repository {

    List<EncounterData> findAllEncounterHTMLTables(String patientId, Date fromDate, Date toDate);
}
