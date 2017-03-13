package uk.gov.hscic.patient.referrals.search;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;
import uk.gov.hscic.patient.referrals.repo.ReferralRepository;

@Service
public class ReferralSearch {

    @Autowired
    private ReferralRepository referralRepository;

    public String findReferralHtml(final String patientId, Date fromDate, Date toDate) {
        List<ReferralEntity> referralEntities;

        if (fromDate != null && toDate != null) {
            referralEntities = referralRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(Long.valueOf(patientId), fromDate, toDate);
        } else if (fromDate != null) {
            referralEntities = referralRepository.findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(Long.valueOf(patientId), fromDate);
        } else if (toDate != null) {
            referralEntities = referralRepository.findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(Long.valueOf(patientId), toDate);
        } else {
            referralEntities = referralRepository.findBynhsNumberOrderBySectionDateDesc(Long.valueOf(patientId));
        }

        if (referralEntities.isEmpty()) {
            return null;
        }

        String html = "<div>"; // Opening tag

        for (ReferralEntity referral : referralEntities) {
            html += referral.getHtmlPart(); // Add content
        }

        return html + "</div>"; // Closing tag
    }
}
