package uk.gov.hscic.patient.referrals.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hscic.common.service.AbstractLegacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;
import uk.gov.hscic.patient.referral.search.ReferralSearch;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;
import uk.gov.hscic.patient.referrals.repo.ReferralRepository;

@Service
public class LegacyReferralSearch extends AbstractLegacyService implements ReferralSearch {

    @Autowired
    private ReferralRepository referralRepository;

    @Override
    public List<ReferralListHTML> findAllReferralHTMLTables(final String patientId) {
        final List<ReferralEntity> referralList = referralRepository.findAll();

        return CollectionUtils.collect(referralList, new ReferralEntityToListTransformer(), new ArrayList<>());
    }
}
