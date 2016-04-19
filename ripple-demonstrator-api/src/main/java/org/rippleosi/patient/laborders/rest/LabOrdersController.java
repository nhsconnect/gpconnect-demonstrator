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
package org.rippleosi.patient.laborders.rest;

import java.util.List;

import org.rippleosi.common.types.RepoSource;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.laborders.model.LabOrderDetails;
import org.rippleosi.patient.laborders.model.LabOrderSummary;
import org.rippleosi.patient.laborders.search.LabOrderSearch;
import org.rippleosi.patient.laborders.search.LabOrderSearchFactory;
import org.rippleosi.patient.laborders.store.LabOrderStore;
import org.rippleosi.patient.laborders.store.LabOrderStoreFactory;
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
@RequestMapping("/patients/{patientId}/laborders")
public class LabOrdersController {

    @Autowired
    private LabOrderSearchFactory labOrderSearchFactory;

    @Autowired
    private LabOrderStoreFactory labOrderStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<LabOrderSummary> findAllLabOrders(@PathVariable("patientId") String patientId,
                                                  @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final LabOrderSearch labOrderSearch = labOrderSearchFactory.select(sourceType);

        return labOrderSearch.findAllLabOrders(patientId);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public LabOrderDetails findLabOrder(@PathVariable("patientId") String patientId,
                                        @PathVariable("orderId") String orderId,
                                        @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final LabOrderSearch labOrderSearch = labOrderSearchFactory.select(sourceType);

        return labOrderSearch.findLabOrder(patientId, orderId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createLabOrders(@PathVariable("patientId") String patientId,
                                @RequestParam(required = false) String source,
                                @RequestBody List<LabOrderDetails> labOrders) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final LabOrderStore labOrderStore = labOrderStoreFactory.select(sourceType);

        labOrderStore.create(patientId, labOrders);
    }
}
