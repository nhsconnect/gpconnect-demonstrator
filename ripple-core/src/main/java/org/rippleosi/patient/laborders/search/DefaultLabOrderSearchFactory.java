package org.rippleosi.patient.laborders.search;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultLabOrderSearchFactory extends AbstractRepositoryFactory<LabOrderSearch> implements LabOrderSearchFactory {

    @Override
    protected LabOrderSearch defaultRepository() {
        return new NotConfiguredLabOrderSearch();
    }

    @Override
    protected Class<LabOrderSearch> repositoryClass() {
        return LabOrderSearch.class;
    }
}
