package uk.gov.hscic.patient.allergies.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.allergies.model.AllergyData;

/**
 */
public interface AllergySearch extends Repository {

   List<AllergyData> findAllAllergyHTMLTables(String patientId);
}
