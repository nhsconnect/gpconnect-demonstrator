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
package org.rippleosi.patient.transfers.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareEntity;
import org.rippleosi.patient.transfers.model.TransferOfCareSummary;
import org.rippleosi.patient.transfers.model.TransferOfCareSummaryEntity;
import org.rippleosi.patient.transfers.repo.TransferOfCareRepository;
import org.rippleosi.patient.transfers.repo.TransferOfCareSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LegacyTransferSearch implements TransferOfCareSearch {

    @Value("${legacy.datasource.priority:900}")
    private int priority;

    @Autowired
    private TransferOfCareRepository transferOfCareRepository;

    @Autowired
    private TransferOfCareSummaryRepository transferOfCareSummaryRepository;

    @Autowired
    private TransferEntityToSummaryTransformer transferEntityToSummaryTransformer;

    @Autowired
    private TransferEntityToDetailsTransformer transferEntityToDetailsTransformer;

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.LEGACY;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public List<TransferOfCareSummary> findAllTransfers(String patientId) {
        List<TransferOfCareSummaryEntity> transfers = transferOfCareSummaryRepository.findAllByPatientId(patientId);
        return CollectionUtils.collect(transfers, transferEntityToSummaryTransformer, new ArrayList<>());
    }

    @Override
    public TransferOfCareDetails findTransferOfCare(String patientId, String transferId) {
        Long id = Long.valueOf(transferId);
        TransferOfCareEntity transfer = transferOfCareRepository.findOne(id);
        return transferEntityToDetailsTransformer.transform(transfer);
    }
}
