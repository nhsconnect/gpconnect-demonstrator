package org.rippleosi.patient.allergies.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.allergies.model.AllergyDetails;
import org.rippleosi.patient.allergies.model.AllergyHeadline;
import org.rippleosi.patient.allergies.model.AllergySummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRAllergySearch extends AbstractOpenEhrService implements AllergySearch {

    @Override
    public List<AllergyHeadline> findAllergyHeadlines(String patientId) {
        AllergyHeadlineQueryStrategy query = new AllergyHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public List<AllergySummary> findAllAllergies(String patientId) {
        AllergySummaryQueryStrategy query = new AllergySummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public AllergyDetails findAllergy(String patientId, String allergyId) {
        AllergyDetailsQueryStrategy query = new AllergyDetailsQueryStrategy(patientId, allergyId);

        return findData(query);
    }
}
