package uk.gov.hscic.patient.referral.rest;

import java.util.List;

import uk.gov.hscic.common.types.RepoSource;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.referral.search.ReferralSearch;
import uk.gov.hscic.patient.referral.search.ReferralSearchFactory;
import uk.gov.hscic.patient.referral.store.ReferralStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/referrals")
public class ReferralsController {

    @Autowired
    private ReferralSearchFactory referralSearchFactory;

    @Autowired
    private ReferralStoreFactory referralStoreFactory;
    
    @RequestMapping(value="/htmlTables", method = RequestMethod.GET)
    public List<ReferralListHTML> findAllReferralHTMLTables(@PathVariable("patientId") String patientId,
                                                @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final ReferralSearch referralSearch = referralSearchFactory.select(sourceType);

        return referralSearch.findAllReferralHTMLTables(patientId);
    }
}
