package uk.gov.hscic.patient.allergies.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;

/**
 */
public interface AllergySearch extends Repository {

   List<AllergyListHTML> findAllAllergyHTMLTables(String patientId);
}
