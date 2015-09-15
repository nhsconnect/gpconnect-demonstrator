package org.rippleosi.patient.referral.search;

import org.rippleosi.common.search.AbstractRepositoryFactory;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class DefaultReferralSearchFactory extends AbstractRepositoryFactory<ReferralSearch> implements ReferralSearchFactory {

    @Override
    protected ReferralSearch defaultRepository() {
        return new NotConfiguredReferralSearch();
    }

    @Override
    protected Class<ReferralSearch> repositoryClass() {
        return ReferralSearch.class;
    }
}
