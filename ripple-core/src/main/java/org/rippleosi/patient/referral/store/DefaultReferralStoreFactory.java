package org.rippleosi.patient.referral.store;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultReferralStoreFactory extends AbstractRepositoryFactory<ReferralStore> implements ReferralStoreFactory {

    @Override
    protected ReferralStore defaultRepository() {
        return new NotConfiguredReferralStore();
    }

    @Override
    protected Class<ReferralStore> repositoryClass() {
        return ReferralStore.class;
    }
}
