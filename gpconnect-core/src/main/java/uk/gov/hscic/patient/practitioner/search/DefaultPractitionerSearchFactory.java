package uk.gov.hscic.patient.practitioner.search;

import uk.gov.hscic.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultPractitionerSearchFactory extends AbstractRepositoryFactory<PractitionerSearch> implements PractitionerSearchFactory {

    @Override
    protected PractitionerSearch defaultRepository() {
        return new NotConfiguredPractitionerSearch();
    }

    @Override
    protected Class<PractitionerSearch> repositoryClass() {
        return PractitionerSearch.class;
    }
}
