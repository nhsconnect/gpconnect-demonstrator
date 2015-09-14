package org.rippleosi.patient.allergies.search;

import java.util.List;

import org.rippleosi.common.search.Repository;
import org.rippleosi.patient.allergies.model.AllergyDetails;
import org.rippleosi.patient.allergies.model.AllergyHeadline;
import org.rippleosi.patient.allergies.model.AllergySummary;

/**
 */
public interface AllergySearch extends Repository {

    List<AllergyHeadline> findAllergyHeadlines(String patientId);

    List<AllergySummary> findAllAllergies(String patientId);

    AllergyDetails findAllergy(String patientId, String allergyId);

}
