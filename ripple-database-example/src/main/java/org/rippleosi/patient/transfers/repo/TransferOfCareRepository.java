package org.rippleosi.patient.transfers.repo;

import java.util.List;

import org.rippleosi.patient.transfers.model.TransferOfCareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferOfCareRepository extends JpaRepository<TransferOfCareEntity, Long> {

    @Query("SELECT toc FROM TransferOfCareEntity toc WHERE toc.sourceId=:sourceId")
	TransferOfCareEntity findBySourceId(@Param("sourceId") String sourceId);

    @Query("SELECT toc FROM TransferOfCareEntity toc WHERE toc.patient.id=:patientId")
    List<TransferOfCareEntity> findAllByPatientId(@Param("patientId") String patientId);
}
