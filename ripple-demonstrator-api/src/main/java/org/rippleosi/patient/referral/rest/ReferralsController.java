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
package org.rippleosi.patient.referral.rest;

import java.util.List;

import org.rippleosi.patient.referral.model.ReferralDetails;
import org.rippleosi.patient.referral.model.ReferralSummary;
import org.rippleosi.patient.referral.search.ReferralSearch;
import org.rippleosi.patient.referral.search.ReferralSearchFactory;
import org.rippleosi.patient.referral.store.ReferralStore;
import org.rippleosi.patient.referral.store.ReferralStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/referrals")
public class ReferralsController {

    @Autowired
    private ReferralSearchFactory referralSearchFactory;

    @Autowired
    private ReferralStoreFactory referralStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<ReferralSummary> findAllReferrals(@PathVariable("patientId") String patientId,
                                                  @RequestParam(required = false) String source) {

        ReferralSearch referralSearch = referralSearchFactory.select(source);

        return referralSearch.findAllReferrals(patientId);
    }

    @RequestMapping(value = "/{referralId}", method = RequestMethod.GET)
    public ReferralDetails findReferral(@PathVariable("patientId") String patientId,
                                        @PathVariable("referralId") String referralId,
                                        @RequestParam(required = false) String source) {

        ReferralSearch referralSearch = referralSearchFactory.select(source);

        return referralSearch.findReferral(patientId, referralId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createPatientReferral(@PathVariable("patientId") String patientId,
                                      @RequestParam(required = false) String source,
                                      @RequestBody ReferralDetails referral) {

        ReferralStore referralStore = referralStoreFactory.select(source);

        referralStore.create(patientId, referral);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updatePatientReferral(@PathVariable("patientId") String patientId,
                                      @RequestParam(required = false) String source,
                                      @RequestBody ReferralDetails referral) {

        ReferralStore referralStore = referralStoreFactory.select(source);

        referralStore.update(patientId, referral);
    }
}
