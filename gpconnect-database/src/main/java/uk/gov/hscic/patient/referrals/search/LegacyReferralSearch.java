package uk.gov.hscic.patient.referrals.search;

import java.util.ArrayList;
import java.util.Collections;
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

    private final ReferralEntityToListTransformer transformer = new ReferralEntityToListTransformer();

    @Override
    public List<ReferralListHTML> findAllReferralHTMLTables(final String patientId) {

        final ReferralEntity item = referralRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
