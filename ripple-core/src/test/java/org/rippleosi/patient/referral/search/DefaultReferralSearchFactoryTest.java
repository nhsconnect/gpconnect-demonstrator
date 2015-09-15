package org.rippleosi.patient.referral.search;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rippleosi.common.search.AbstractRepositoryFactoryTest;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultReferralSearchFactoryTest extends AbstractRepositoryFactoryTest<ReferralSearchFactory, ReferralSearch> {

    @Override
    protected ReferralSearchFactory createRepositoryFactory() {
        return new DefaultReferralSearchFactory();
    }

    @Override
    protected Class<ReferralSearch> getRepositoryClass() {
        return ReferralSearch.class;
    }
}
