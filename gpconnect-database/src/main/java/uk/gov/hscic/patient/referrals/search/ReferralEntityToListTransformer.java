package uk.gov.hscic.patient.referrals.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;

public class ReferralEntityToListTransformer implements Transformer<ReferralEntity, ReferralListHTML> {

    @Override
    public ReferralListHTML transform(final ReferralEntity referralEntity) {
        final ReferralListHTML referralList = new ReferralListHTML();

        referralList.setSourceId(String.valueOf(referralEntity.getId()));
        referralList.setSource(RepoSourceType.LEGACY.getSourceName());

        referralList.setProvider(referralEntity.getProvider());
        referralList.setHtml(referralEntity.getHtml());

        referralList.setLastUpdated(referralEntity.getLastUpdated());
        return referralList;
    }
}
