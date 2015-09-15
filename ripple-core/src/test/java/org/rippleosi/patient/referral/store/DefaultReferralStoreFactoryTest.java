package org.rippleosi.patient.referral.store;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultReferralStoreFactoryTest extends AbstractRepositoryFactoryTest<ReferralStoreFactory, ReferralStore> {

    @Override
    protected ReferralStoreFactory createRepositoryFactory() {
        return new DefaultReferralStoreFactory();
    }

    @Override
    protected Class<ReferralStore> getRepositoryClass() {
        return ReferralStore.class;
    }
}
