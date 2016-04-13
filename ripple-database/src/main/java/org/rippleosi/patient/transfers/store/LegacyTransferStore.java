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
package org.rippleosi.patient.transfers.store;

import org.apache.camel.Body;
import org.apache.camel.Consume;
import org.apache.camel.Header;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.details.repo.PatientRepository;
import org.rippleosi.patient.transfers.model.TransferOfCareDetails;
import org.rippleosi.patient.transfers.model.TransferOfCareEntity;
import org.rippleosi.patient.transfers.repo.TransferOfCareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LegacyTransferStore implements TransferOfCareStore {

    @Value("${legacy.datasource.priority:900}")
    private int priority;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TransferOfCareRepository transferOfCareRepository;

    @Override
    public RepoSourceType getSource() {
        return RepoSourceType.LEGACY;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    @Consume(uri = "activemq:Consumer.Local.VirtualTopic.Legacy.Transfers.Create")
    public void create(@Header("patientId") String patientId, @Body TransferOfCareDetails transferOfCare) {
        PatientEntity patientEntity = patientRepository.findByNhsNumber(patientId);

        TransferOfCareEntity transferEntity = new TransferDetailsToEntityTransformer().transform(transferOfCare);
        transferEntity.setPatient(patientEntity);

        transferOfCareRepository.save(transferEntity);
    }
}
