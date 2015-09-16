package org.rippleosi.patient.labresults.search;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultLabResultSearchFactory extends AbstractRepositoryFactory<LabResultSearch> implements LabResultSearchFactory {

    @Override
    protected LabResultSearch defaultRepository() {
        return new NotConfiguredLabResultSearch();
    }

    @Override
    protected Class<LabResultSearch> repositoryClass() {
        return LabResultSearch.class;
    }
}
