package org.rippleosi.patient.allergies.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.allergies.model.AllergyDetails;
import org.rippleosi.patient.allergies.model.AllergyHeadline;
import org.rippleosi.patient.allergies.model.AllergySummary;

/**
 */
public class NotConfiguredAllergySearch implements AllergySearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<AllergyHeadline> findAllergyHeadlines(String patientId) {
        throw ConfigurationException.unimplementedTransaction(AllergySearch.class);
    }

    @Override
    public List<AllergySummary> findAllAllergies(String patientId) {
        throw ConfigurationException.unimplementedTransaction(AllergySearch.class);
    }

    @Override
    public AllergyDetails findAllergy(String patientId, String allergyId) {
        throw ConfigurationException.unimplementedTransaction(AllergySearch.class);
    }
}
