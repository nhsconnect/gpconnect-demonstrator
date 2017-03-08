package uk.gov.hscic.patient.referrals.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.referral.model.ReferralListHtml;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;

public class ReferralEntityToListTransformer implements Transformer<ReferralEntity, ReferralListHtml> {

    @Override
    public ReferralListHtml transform(final ReferralEntity referralEntity) {
        final ReferralListHtml referralList = new ReferralListHtml();

        referralList.setSourceId(String.valueOf(referralEntity.getId()));
        referralList.setSource(RepoSourceType.LEGACY.getSourceName());

        referralList.setProvider(referralEntity.getProvider());
       // referralList.setHtml(referralEntity.getHtml());

        referralList.setLastUpdated(referralEntity.getLastUpdated());
        return referralList;
    }
}
