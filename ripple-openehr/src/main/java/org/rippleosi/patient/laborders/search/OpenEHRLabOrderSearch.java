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
package org.rippleosi.patient.laborders.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.rippleosi.patient.laborders.model.LabOrderSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRLabOrderSearch extends AbstractOpenEhrService implements LabOrderSearch {

    @Override
    public List<LabOrderSummary> findAllLabOrders(String patientId) {
        LabOrderSummaryQueryStrategy query = new LabOrderSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public LabOrderDetails findLabOrder(String patientId, String labOrderId) {
        LabOrderDetailsQueryStrategy query = new LabOrderDetailsQueryStrategy(patientId, labOrderId);

        return findData(query);
    }
}
