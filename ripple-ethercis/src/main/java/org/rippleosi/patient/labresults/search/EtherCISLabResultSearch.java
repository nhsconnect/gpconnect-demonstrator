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
package org.rippleosi.patient.labresults.search;

import java.util.List;

import org.rippleosi.common.service.AbstractEtherCISService;
import org.rippleosi.patient.labresults.model.LabResultDetails;
import org.rippleosi.patient.labresults.model.LabResultSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class EtherCISLabResultSearch extends AbstractEtherCISService implements LabResultSearch {

    @Override
    public List<LabResultSummary> findAllLabResults(String patientId) {
        EtherCISLabResultSummaryQueryStrategy query = new EtherCISLabResultSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public LabResultDetails findLabResult(String patientId, String labResultId) {
        EtherCISLabResultDetailsQueryStrategy query = new EtherCISLabResultDetailsQueryStrategy(patientId, labResultId);

        return findData(query);
    }
}
