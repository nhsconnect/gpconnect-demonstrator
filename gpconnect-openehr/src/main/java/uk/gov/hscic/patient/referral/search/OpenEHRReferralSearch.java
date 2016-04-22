/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package uk.gov.hscic.patient.referral.search;

import java.util.List;

import uk.gov.hscic.common.service.AbstractOpenEhrService;
import uk.gov.hscic.patient.referral.model.ReferralDetails;
import uk.gov.hscic.patient.referral.model.ReferralSummary;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.exception.ConfigurationException;
import uk.gov.hscic.patient.referral.model.ReferralListHTML;

/**
 */
@Service
public class OpenEHRReferralSearch extends AbstractOpenEhrService implements ReferralSearch {

    public List<ReferralSummary> findAllReferrals(String patientId) {
        ReferralSummaryQueryStrategy query = new ReferralSummaryQueryStrategy(patientId);

        return findData(query);
    }

    public ReferralDetails findReferral(String patientId, String referralId) {
        ReferralDetailsQueryStrategy query = new ReferralDetailsQueryStrategy(patientId, referralId);

        return findData(query);
    }

    @Override
    public List<ReferralListHTML> findAllReferralHTMLTables(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ReferralSearch.class);
    }
}
