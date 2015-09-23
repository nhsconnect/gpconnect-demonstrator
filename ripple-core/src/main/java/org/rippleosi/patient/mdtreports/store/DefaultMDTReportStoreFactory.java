package org.rippleosi.patient.mdtreports.store;

import org.rippleosi.common.repo.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultMDTReportStoreFactory extends AbstractRepositoryFactory<MDTReportStore> implements MDTReportStoreFactory {

    @Override
    protected MDTReportStore defaultRepository() {
        return new NotConfiguredMDTReportStore();
    }

    @Override
    protected Class<MDTReportStore> repositoryClass() {
        return MDTReportStore.class;
    }
}
