package org.rippleosi.patient.allergies.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultAllergySearchFactory extends AbstractRepositoryFactory<AllergySearch> implements AllergySearchFactory {

    @Override
    protected AllergySearch defaultRepository() {
        return new NotConfiguredAllergySearch();
    }

    @Override
    protected Class<AllergySearch> repositoryClass() {
        return AllergySearch.class;
    }
}
