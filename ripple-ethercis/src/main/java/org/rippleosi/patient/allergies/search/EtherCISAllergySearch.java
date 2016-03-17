/*
 *  Copyright 2015 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */

package org.rippleosi.patient.allergies.search;

import java.util.List;

import org.rippleosi.common.service.AbstractEtherCISService;
import org.rippleosi.patient.allergies.model.AllergyDetails;
import org.rippleosi.patient.allergies.model.AllergyHeadline;
import org.rippleosi.patient.allergies.model.AllergySummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class EtherCISAllergySearch extends AbstractEtherCISService implements AllergySearch {

    @Override
    public List<AllergyHeadline> findAllergyHeadlines(String patientId) {
        EtherCISAllergyHeadlineQueryStrategy query = new EtherCISAllergyHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public List<AllergySummary> findAllAllergies(String patientId) {
        EtherCISAllergySummaryQueryStrategy query = new EtherCISAllergySummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public AllergyDetails findAllergy(String patientId, String allergyId) {
        EtherCISAllergyDetailsQueryStrategy query = new EtherCISAllergyDetailsQueryStrategy(patientId, allergyId);

        return findData(query);
    }
}
