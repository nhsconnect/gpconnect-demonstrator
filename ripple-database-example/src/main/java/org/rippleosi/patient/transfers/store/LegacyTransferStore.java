package org.rippleosi.patient.transfers.store;

import org.apache.camel.Body;
import org.apache.camel.Consume;
import org.apache.camel.Header;
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
    public String getSource() {
        return "legacy";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    @Consume(uri = "activemq:topic:VirtualTopic.Ripple.Transfers.Create")
    public void create(@Header("patientId") String patientId, @Body TransferOfCareDetails transferDetails) {
        PatientEntity patientEntity = patientRepository.findByPatientId(patientId);

        TransferOfCareEntity transferEntity = new TransferDetailsToEntityTransformer().transform(transferDetails);
        transferEntity.setPatient(patientEntity);

        transferOfCareRepository.save(transferEntity);
    }
}
