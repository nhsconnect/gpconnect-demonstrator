package uk.gov.hscic.patient.referrals.search;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    private final ReferralEntityToHTMLTransformer transformer = new ReferralEntityToHTMLTransformer();

    @Override
    public List<ReferralListHTML> findAllReferralHTMLTables(final String patientId,Date fromDate,
            Date toDate) {

       List<ReferralEntity> items = null;
       if (fromDate != null && toDate != null) {
           items = referralRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(
                   Long.valueOf(patientId), fromDate, toDate);
       } else if (fromDate != null) {
           items = referralRepository
                   .findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(Long.valueOf(patientId), fromDate);
       } else if (toDate != null) {
           items = referralRepository
                   .findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(Long.valueOf(patientId), toDate);
       } else {
           items = referralRepository.findBynhsNumberOrderBySectionDateDesc(Long.valueOf(patientId));
       }
       if (items  == null || items.size() <= 0) {
           return null;
       } else {
           return Collections.singletonList(transformer.transform(items));
       }
   }
}
