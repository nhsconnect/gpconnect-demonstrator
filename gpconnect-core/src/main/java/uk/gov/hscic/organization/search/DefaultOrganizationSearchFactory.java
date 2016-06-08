package uk.gov.hscic.organization.search;

import uk.gov.hscic.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultOrganizationSearchFactory extends AbstractRepositoryFactory<OrganizationSearch> implements OrganizationSearchFactory {

    @Override
    protected OrganizationSearch defaultRepository() {
        return new NotConfiguredOrganizationSearch();
    }

    @Override
    protected Class<OrganizationSearch> repositoryClass() {
        return OrganizationSearch.class;
    }
}
