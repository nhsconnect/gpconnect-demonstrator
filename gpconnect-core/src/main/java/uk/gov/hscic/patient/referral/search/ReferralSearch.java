package uk.gov.hscic.patient.referral.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;

/**
 */
public interface ReferralSearch extends Repository {

    List<ReferralListHTML> findAllReferralHTMLTables(String patientId);

}
