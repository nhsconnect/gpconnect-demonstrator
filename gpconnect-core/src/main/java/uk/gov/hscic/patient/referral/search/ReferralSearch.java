package uk.gov.hscic.patient.referral.search;

import java.util.Date;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.referral.model.ReferralListHtml;

public interface ReferralSearch extends Repository {
    ReferralListHtml findReferralListHtml(String patientId, Date fromDate, Date toDate);
}
