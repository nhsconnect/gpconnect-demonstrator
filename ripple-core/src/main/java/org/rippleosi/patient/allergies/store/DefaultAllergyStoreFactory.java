package org.rippleosi.patient.allergies.store;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultAllergyStoreFactory extends AbstractRepositoryFactory<AllergyStore> implements AllergyStoreFactory {

    @Override
    protected AllergyStore defaultRepository() {
        return new NotConfiguredAllergyStore();
    }

    @Override
    protected Class<AllergyStore> repositoryClass() {
        return AllergyStore.class;
    }
}
