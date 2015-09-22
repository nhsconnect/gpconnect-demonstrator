package org.rippleosi.patient.transfers.repo;

import org.rippleosi.patient.transfers.model.TransferOfCareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferOfCareRepository extends JpaRepository<TransferOfCareEntity, Long> {

}
