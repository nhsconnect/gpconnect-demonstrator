package org.rippleosi.patient.terminology.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultTerminologySearchFactory extends AbstractRepositoryFactory<TerminologySearch>
    implements TerminologySearchFactory {

    @Override
    protected TerminologySearch defaultRepository() {
        return new NotConfiguredTerminologySearch();
    }

    @Override
    protected Class<TerminologySearch> repositoryClass() {
        return TerminologySearch.class;
    }
}
