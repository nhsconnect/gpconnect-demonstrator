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
package org.rippleosi.patient.transfers.rest;

import java.util.List;

import org.rippleosi.common.types.RepoSource;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;
import org.rippleosi.patient.transfers.search.TransferOfCareSearch;
import org.rippleosi.patient.transfers.search.TransferOfCareSearchFactory;
import org.rippleosi.patient.transfers.store.TransferOfCareStore;
import org.rippleosi.patient.transfers.store.TransferOfCareStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients/{patientId}/transfers-of-care")
public class TransfersOfCareController {

    @Autowired
    private TransferOfCareSearchFactory transferOfCareSearchFactory;

    @Autowired
    private TransferOfCareStoreFactory transferOfCareStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<TransferOfCareSummary> findAllTransfersOfCare(@PathVariable("patientId") String patientId,
                                                              @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final TransferOfCareSearch search = transferOfCareSearchFactory.select(sourceType);

        return search.findAllTransfers(patientId);
    }

    @RequestMapping(value = "/{transferId}", method = RequestMethod.GET)
    public TransferOfCareDetails findTransferOfCare(@PathVariable("patientId") String patientId,
                                                    @PathVariable("transferId") String transferId,
                                                    @RequestParam(required = false) String source) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final TransferOfCareSearch search = transferOfCareSearchFactory.select(sourceType);

        return search.findTransferOfCare(patientId, transferId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createTransferOfCare(@PathVariable("patientId") String patientId,
                                     @RequestParam(required = false) String source,
                                     @RequestBody TransferOfCareDetails transferOfCare) {
        final RepoSource sourceType = RepoSourceType.fromString(source);
        final TransferOfCareStore store = transferOfCareStoreFactory.select(sourceType);

        store.create(patientId, transferOfCare);
    }
}
