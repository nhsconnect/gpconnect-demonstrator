package uk.gov.hscic.patient.clinicalitems.search;

import org.springframework.stereotype.Service;
import uk.gov.hscic.common.repo.AbstractRepositoryFactory;

@Service
public class DefaultClinicalItemSearchFactory extends AbstractRepositoryFactory<ClinicalItemSearch> implements ClinicalItemSearchFactory {

    @Override
    protected ClinicalItemSearch defaultRepository() {
        return new NotConfiguredClinicalItemSearch();
    }

    @Override
    protected Class<ClinicalItemSearch> repositoryClass() {
        return ClinicalItemSearch.class;
    }
}
