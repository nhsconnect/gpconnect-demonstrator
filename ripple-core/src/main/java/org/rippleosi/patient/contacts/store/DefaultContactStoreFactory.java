package org.rippleosi.patient.contacts.store;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultContactStoreFactory extends AbstractRepositoryFactory<ContactStore> implements ContactStoreFactory {

    @Override
    protected ContactStore defaultRepository() {
        return new NotConfiguredContactStore();
    }

    @Override
    protected Class<ContactStore> repositoryClass() {
        return ContactStore.class;
    }
}
