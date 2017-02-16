package uk.gov.hscic.patient.allergies.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.allergies.model.AllergyDetails;
import uk.gov.hscic.patient.allergies.model.AllergyHeadline;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.model.AllergySummary;

/**
 */
public interface AllergySearch extends Repository {

   List<AllergyListHTML> findAllAllergyHTMLTables(String patientId);
}
