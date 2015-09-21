package org.rippleosi.patient.contacts.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultContactSearchFactory extends AbstractRepositoryFactory<ContactSearch> implements ContactSearchFactory {

    @Override
    protected ContactSearch defaultRepository() {
        return new NotConfiguredContactSearch();
    }

    @Override
    protected Class<ContactSearch> repositoryClass() {
        return ContactSearch.class;
    }
}
