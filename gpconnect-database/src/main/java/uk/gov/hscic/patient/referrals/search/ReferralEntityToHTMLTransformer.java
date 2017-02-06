package uk.gov.hscic.patient.referrals.search;

import java.util.List;

import org.apache.commons.collections4.Transformer;

import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;

public class ReferralEntityToHTMLTransformer implements Transformer<List<ReferralEntity>, ReferralListHTML> {

    @Override
    public ReferralListHTML transform(final List<ReferralEntity> referralEntity) {
        final ReferralListHTML referralList = new ReferralListHTML();
        referralList.setSource(String.valueOf(referralEntity.get(0).getId()));
        referralList.setSource(RepoSourceType.LEGACY.getSourceName());
        
        referralList.setProvider(referralEntity.get(0).getProvider());
        
        String html = "<div>"; // Opening tag
        for(ReferralEntity referral : referralEntity){
            html = html + referral.getHtmlPart(); // Add content
        }
        html = html + "</div>"; // Closing tag
        referralList.setHtml(html);
        
        referralList.setLastUpdated(referralEntity.get(0).getLastUpdated());
        return referralList;
    }

}
