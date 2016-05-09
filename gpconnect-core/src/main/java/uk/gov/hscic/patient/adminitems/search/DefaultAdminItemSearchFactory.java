package uk.gov.hscic.patient.adminitems.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultAdminItemSearchFactory extends AbstractRepositoryFactory<AdminItemSearch> implements AdminItemSearchFactory {

    @Override
    protected AdminItemSearch defaultRepository() {
        return new NotConfiguredAdminItemSearch();
    }

    @Override
    protected Class<AdminItemSearch> repositoryClass() {
        return AdminItemSearch.class;
    }
}
