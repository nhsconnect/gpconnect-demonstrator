package uk.gov.hscic.patient.referrals.search;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.referral.model.ReferralListHtml;
import uk.gov.hscic.patient.referrals.model.ReferralEntity;
import uk.gov.hscic.patient.referrals.repo.ReferralRepository;

@Service
public class ReferralSearch {
    private final ReferralEntityToHTMLTransformer transformer = new ReferralEntityToHTMLTransformer();

    @Autowired
    private ReferralRepository referralRepository;

    public ReferralListHtml findReferralListHtml(final String patientId, Date fromDate, Date toDate) {
       List<ReferralEntity> items;

       if (fromDate != null && toDate != null) {
           items = referralRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(Long.valueOf(patientId), fromDate, toDate);
       } else if (fromDate != null) {
           items = referralRepository.findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(Long.valueOf(patientId), fromDate);
       } else if (toDate != null) {
           items = referralRepository.findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(Long.valueOf(patientId), toDate);
       } else {
           items = referralRepository.findBynhsNumberOrderBySectionDateDesc(Long.valueOf(patientId));
       }

       return items.isEmpty()
               ? null
               : transformer.transform(items);
   }
}
